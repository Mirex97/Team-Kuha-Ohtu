#!/usr/bin/python3
import csv
import sqlite3
import pyisbn
from collections import namedtuple

def validate(isbn):
  try:
    return pyisbn.validate(isbn)
  except:
    return False

def convert(isbn):
  if len(isbn) != 13:
    return pyisbn.convert(isbn)
  return isbn

Entry = namedtuple("Entry", "id isbn title author year publisher")

print("Opening database")
db = sqlite3.connect("bookmarks.db")
c = db.cursor()

idc = c.execute("SELECT MAX(id) FROM entry").fetchone()[0] or 0

def next_id():
  global idc
  idc += 1
  return idc

print("Reading entries")
with open("BX-Books.csv") as file:
  reader = csv.reader(file, delimiter=";", quotechar='"')
  data = [Entry(id=next_id(),
                isbn=convert(row[0]),
                title=row[1],
                author=row[2],
                year=row[3],
                publisher=row[4])
          for row in reader
          if validate(row[0])]

ids = [(entry.id,) for entry in data]
print("Inserting entry IDs")
c.executemany("INSERT INTO entry (id, read) VALUES (?, false)", ids)

print("Processing tags")
idc = c.execute("SELECT MAX(id) FROM tag").fetchone()[0] or 0
publisher_map = {}
year_map = {}
for entry in data:
  if entry.year not in year_map:
    year_map[entry.year] = next_id()
  if entry.publisher not in publisher_map:
    publisher_map[entry.publisher] = next_id()
years = [(id, "year", year) for year, id in year_map.items()]
publishers = [(id, "publisher", publisher) for publisher, id in publisher_map.items()]
tags = years + publishers
print("Inserting tags")
c.executemany("INSERT INTO tag (id, `type`, `name`) VALUES (?, ?, ?)", tags)

print("Processing entry tags")
entry_years = [(entry.id, year_map[entry.year]) for entry in data]
entry_publishers = [(entry.id, publisher_map[entry.publisher]) for entry in data]
entry_tags = entry_years + entry_publishers
print("Inserting entry tags")
c.executemany("INSERT INTO entry_tag (entry_id, tag_id) VALUES (?, ?)", entry_tags)

print("Processing entry metadata")
types = [(entry.id, "type", "book") for entry in data]
isbns = [(entry.id, "ISBN", entry.isbn) for entry in data]
titles = [(entry.id, "Title", entry.title) for entry in data]
authors = [(entry.id, "Author", entry.author) for entry in data]

metadata = types + isbns + titles + authors
print("Inserting entry metadata")
c.executemany("INSERT INTO entry_metadata (entry_id, key, value) VALUES (?, ?, ?)", metadata)

print("Committing changes")
db.commit()

print("All done")
