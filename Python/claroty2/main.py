import time  # important libraries
import unittest
from selenium import webdriver

searchEngine = 'http://www.google.com'  # desired site and search engine
desiredWebsite = 'https://www.claroty.com/'
searchTerm = 'claroty'  # desired search term


class SearchText(unittest.TestCase):

    @classmethod
    def setUpClass(self):
        self.driver = webdriver.Chrome()  # create a new Chrome session
        self.driver.maximize_window()
        self.driver.get("http://www.google.com/")  # navigate to the application home page

    def test_print_results_number(self):
        search = self.driver.find_element_by_name('q')
        search.send_keys(searchTerm)  # search for desired website
        search.submit()
        results = len(self.driver.find_elements_by_class_name('iUh30'))  # catch google results
        print('The number of google results is: ' + str(results))
        time.sleep(3)

    def test_verify_first(self):
        if not (self.driver.find_element_by_tag_name(
                "cite").text == desiredWebsite):  # catch first result and abort if not Claroty
            print(desiredWebsite + ' should be the first result!\nStart over please')
            quit()

    def test_verify_jobs(self):
        self.driver.find_element_by_link_text('Careers').click()  # go to careers
        careers = self.driver.find_elements_by_class_name("w-dyn-item")  # find job list
        index = 0
        jobs = len(careers)
        for career in careers:
            if str(careers[index].size) != "{'height': 38, 'width': 1100}":  # ignore the invisible test and print
                jobs = jobs - 1
            index = index + 1
        print('The number of careers is: ' + str(jobs))
        time.sleep(5)  # sleep for 5 seconds so you can see the results
        self.driver.quit()


if __name__ == '__main__':
    unittest.main()
