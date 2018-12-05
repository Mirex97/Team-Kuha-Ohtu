#!/usr/bin/python3
from urllib.request import urlopen
import json

next = "http://gutendex.com/books/"
try:
  with open("next") as file:
    next = file.read()
except:
  pass

books = []
try:
  with open("gutendex.json") as file:
    books = json.load(file)
except:
  pass
n = int(len(books) / 32)
total = 0

def flush():
  print(f"Downloaded {n} pages with {len(books)} books out of {total} books", flush=False)
  print("Writing downloaded index to gutendex.json...", end=" ", flush=True)
  with open("gutendex.json", "w") as file:
    json.dump(books, file)
  with open("next", "w") as file:
    file.write(next)
  print("Done.", flush=True)

try:
  while next is not None:
    print(f"Downloading {next}...", end=" ", flush=True)
    with urlopen(next) as resp:
      print(f"{resp.status}, parsing JSON...", end=" ", flush=True)
      data = json.load(resp)
    total = data["count"]
    next = data["next"]
    print("appending results to list...", end=" ", flush=True)
    books += data["results"]
    print("Done.", flush=True)
    n += 1
    if n % 10 == 0:
      flush()
except:
  print("")
  pass

flush()
print("All done")
