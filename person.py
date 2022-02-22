from dataclasses import dataclass


# This is a dataclass to define an age int, as described in the task
@dataclass
class Person(object):
    age: int = 1
