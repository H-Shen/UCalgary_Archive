#!/Users/hshen/anaconda3/bin/python3.6

# -*- coding: utf-8 -*-

"""
Copy assigned java and junit files (if exist) with absolute paths
to /Users/hshen/Desktop/CPSC233/test/

    Author: Haohu Shen
    Date:   Jan 2019
"""

import random
import sys
import uuid
from faker import Faker


def email(name):
    return name.lower().replace(' ', '_') + "@example.org"


def phoneNumber():
    s = ""
    for i in range(3):
        s = s + str(random.randint(0, 9))
    s += "-"
    for i in range(3):
        s = s + str(random.randint(0, 9))
    s += "-"
    for i in range(4):
        s = s + str(random.randint(0, 9))
    return s


def date_to_string(date):
    s = str(date.year) + "-"
    if date.month < 10:
        s = s + "0" + str(date.month)
    else:
        s = s + str(date.month)
    s = s + "-"
    if date.day < 10:
        s = s + "0" + str(date.day)
    else:
        s = s + str(date.day)
    return s


if __name__ == '__main__':

    if len(sys.argv) == 2:

        N = int(sys.argv[1])
        person = Faker()
        name_set = set()
        addr_set = set()
        emailAddr_set = set()
        uuid_set = set()
        phoneNumber_set = set()

        for time in range(N):

            while True:
                gender = random.randint(0, 1)
                Uuid = str(uuid.uuid4())
                phone_number = phoneNumber()
                psw = person.password()
                date = date_to_string(
                    person.date_of_birth(minimum_age=20, maximum_age=60))

                if gender == 1:
                    gender = 'M'
                    name = person.name_male().upper()
                    addr = person.address().replace('\n', ',')
                    emailAddr = email(name)
                else:
                    gender = 'F'
                    name = person.name_female().upper()
                    addr = person.address().replace('\n', ',')
                    emailAddr = email(name)

                if name in name_set:
                    continue
                name_set.add(name)

                if addr in addr_set:
                    continue
                addr_set.add(name)

                if emailAddr in emailAddr_set:
                    continue
                emailAddr_set.add(name)

                if Uuid in uuid_set:
                    continue
                uuid_set.add(Uuid)

                if phone_number in phoneNumber_set:
                    continue
                phoneNumber_set.add(phone_number)

                statement = "INSERT INTO RANDOM_USER VALUES ("
                statement = statement + "'" + Uuid + "',"
                statement = statement + "'" + name + "',"
                statement = statement + "'" + gender + "',"
                statement = statement + "'" + date + "',"
                statement = statement + "'" + emailAddr + "',"
                statement = statement + "'" + phone_number + "',"
                statement = statement + "'" + addr + "',"
                statement = statement + "'" + psw + "');"

                print(statement)
                break
