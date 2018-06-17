
import csv
import json
import sys
import time
import os
import random
import requests

from time import gmtime, strftime

# HTTP Client for Azure Machine Learning Web Service
# Chris Joakim, Microsoft, 2017/07/24
#
# python http_client.py post 10

url     = 'https://cjoakimadhocfunctions.azurewebsites.net/api/HttpTriggerJS1?code=ucG4FG25aMONKyZjmTm9TxfoQbSBCalyegIOJyKRHltbvkq65qgCvw=='
headers = {'Content-Type':'application/json'}

def read_csv_data():
    data, fields = list(), list()
    with open('postal_codes_nc.csv', 'rt', encoding='utf-8') as f:
        for line_idx, line in enumerate(f):
            tokens = line.strip().split(',')
            if line_idx == 0:
                fields = tokens
            else:
                if len(fields) == len(fields):
                    obj = dict()
                    for field_idx, field in enumerate(fields):
                        obj[field] = tokens[field_idx]
                    data.append(obj)
    return data


if __name__ == "__main__":

    if len(sys.argv) > 2:
        func  = sys.argv[1].lower()
        if func == 'post':
            count = int(sys.argv[2])
            csv_data = read_csv_data()
            print('{} csv rows loaded'.format(len(csv_data)))
            headers = {'Content-Type': 'application/json'}

            for i in range(count):
                ridx = random.randint(0, len(csv_data) - 1)
                obj  = csv_data[ridx]
                jstr = json.dumps(obj)
                print(obj)
                r = requests.post(url, headers=headers, data=jstr)



