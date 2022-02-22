from collections import UserList


# This class defines a Magiclist, using a wrapper for lists.
class MagicList(UserList):
    def __init__(self, cls_type=None):
        super().__init__()
        self._cls_type = cls_type

    # Evaluation of self[key].
    def __getitem__(self, index):
        size = len(self.data)
        if index == -1 or size == index:
            self.data.append(self._cls_type())
        return super().__getitem__(index)

    # Evaluation of self[key] in range.
    def __setitem__(self, index, item):
        size = len(self.data)
        if index == -1 or size == index:
            self.data.append(item)
        else:
            super().__setitem__(index, item)
