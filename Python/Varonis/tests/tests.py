from MagicList import *
from person import *


# This test verifies that our magic list does it's basic function for allowing assignment out of range
def test_magic_list_basic():
    a = MagicList()
    print("a = MagicList()")
    a[0] = 5
    print("a[0] = 5")
    print("print(a)")
    print(a)
    print("==========================================================================================\n")


# This test verifies that our magic list supports initializing assigned types when cls_type is provided to its constructor
def test_magic_list_type():
    a = MagicList(cls_type=Person)
    print("a = MagicList(cls_type=Person)")
    a[0].age = 5
    print('a[0].age = 5')
    print("print(a)")
    print(a)
    print("==========================================================================================\n")


# This test verifies that our magic list enforces its indexes continuity:

def test_magic_list_continuity():
    a = MagicList(cls_type=Person)
    print("a = MagicList(cls_type=Person)")
    a[1].age = 5
    print('a[1].age = 5')
    print(a)
