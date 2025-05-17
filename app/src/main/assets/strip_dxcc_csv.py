"""This script strips the original dxcc-2020-02.csv taken from here
   https://github.com/k0swe/dxcc-json/blob/main/dxcc-2020-02.csv
   into a variant that only contains prefixes and countrys.
   
   Author: Dennis Luebke, 2025
"""

import array
import os
import pandas as pd
import sys

class strip_dxcc:
  """Class to strip the original CSV."""
  def __init__(self, csv_path):
    """Constructor."""
    self.csv_path = csv_path
    self.csv_name = os.path.splitext(os.path.basename(self.csv_path))[0]
    self.output_csv_path = os.path.dirname(self.csv_path) + "/" + self.csv_name + "_stripped.csv"
    print(self.output_csv_path)
    self.output_header = ["prefix", "country"]
    self.strip_csv()
  
  def strip_csv(self):
    """Strips the CSV and writes output."""
    csv = pd.read_csv(self.csv_path)
    print(csv)
    out_rows = []
    for index, row in csv.iterrows():
      country = row["name"]
      prefixes = str(row["prefix"]).split(",")
      print(country + ": " + str(prefixes))
      if len(prefixes) > 0:
        for p in prefixes:
          out_rows.append([p, country])
    for r in out_rows:
      print(r)
    out_csv = pd.DataFrame(out_rows, columns=self.output_header)
    out_csv.to_csv(self.output_csv_path, sep=";", index=False)
    
      

if __name__ == "__main__":
  stripper = strip_dxcc(sys.argv[1])
