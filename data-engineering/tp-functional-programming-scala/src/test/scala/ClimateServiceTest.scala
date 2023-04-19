import com.github.polomarcus.utils.ClimateService
import com.github.polomarcus.model.CO2Record
import org.scalatest.funsuite.AnyFunSuite

//@See https://www.scalatest.org/scaladoc/3.1.2/org/scalatest/funsuite/AnyFunSuite.html
class ClimateServiceTest extends AnyFunSuite {
  test("containsWordGlobalWarming - non climate related words should return false") {
    assert( ClimateService.isClimateRelated("pizza") == false)
    assert( ClimateService.isClimateRelated("rien à voir") == false)
  }

  test("isClimateRelated - climate related words should return true") {
    assert(ClimateService.isClimateRelated("climate change") == true)
    assert(ClimateService.isClimateRelated("IPCC") == true)
  }

  test("isClimateRelated - both") {
    assert(ClimateService.isClimateRelated("TonioElPueblo") == false)
    assert(ClimateService.isClimateRelated("global warming"))
  }

  test("parseRawData") {
    // our inputs
    val firstRecord = (2003, 1, 355.2)     //help: to acces 2003 of this tuple, you can do firstRecord._1
    val secondRecord = (2004, 1, 375.2)
    val list1 = List(firstRecord, secondRecord)

    // our output of our method "parseRawData"
    val co2RecordWithType = CO2Record(firstRecord._1, firstRecord._2, firstRecord._3)
    val co2RecordWithType2 = CO2Record(secondRecord._1, secondRecord._2, secondRecord._3)
    val output = List(Some(co2RecordWithType), Some(co2RecordWithType2))

    // we call our function here to test our input and output
    assert(ClimateService.parseRawData(list1) == output)
  }

  // Test getMinMax function with a list of CO2Records
  test("getMinMax - basic") {
    // define input data
    val records = List(
      CO2Record(2020, 1, 400.0),
      CO2Record(2020, 2, 410.0),
      CO2Record(2020, 3, 390.0),
      CO2Record(2020, 4, 420.0),
      CO2Record(2020, 5, 380.0)
    )

    // define expected output
    val expected = (380.0, 420.0)

    // verify that getMinMax function returns the expected minimum and maximum ppm values
    assert(ClimateService.getMinMax(records) == expected)
  }

  // Test getMinMax function with an empty list, which should throw an exception
  test("getMinMax - empty list should throw IllegalArgumentException") {
    // verify that getMinMax function throws an IllegalArgumentException when given an empty list
    assertThrows[IllegalArgumentException] {
      val list = List.empty[CO2Record]
      val result = ClimateService.getMinMax(list)
    }
  }

  // Test getMinMax function with a list of CO2Records with the same ppm value
  test("getMinMax - list of multiple records with same ppm values") {
    // define input data
    val list = List(
      CO2Record(2020, 1, 400.0),
      CO2Record(2020, 2, 400.0),
      CO2Record(2020, 3, 400.0)
    )

    // define expected output
    val expected = (400.0, 400.0)

    // verify that getMinMax function correctly handles a list with multiple records having the same ppm value
    assert(ClimateService.getMinMax(list) == expected)
  }

  // Test getMinMax function with a list of a single CO2Record
  test("getMinMax - single-record list") {
    // define input data
    val list = List(CO2Record(2020, 1, 400.0))

    // define expected output
    val expected = (400.0, 400.0)

    // verify that getMinMax function correctly handles a list with a single record
    assert(ClimateService.getMinMax(list) == expected)
  }

  // Test getMinMaxByYear function with a list of CO2Records
  test("getMinMaxByYear - basic") {
    // define input data
    val records = List(
      CO2Record(2021, 1, 415.2),
      CO2Record(2021, 2, 414.9),
      CO2Record(2021, 3, 415.8),
      CO2Record(2022, 1, 410.2),
      CO2Record(2022, 2, 408.9),
      CO2Record(2022, 3, 412.8)
    )

    // define expected output
    val (min, max) = ClimateService.getMinMaxByYear(records, 2021)
    val expected = (414.9, 415.8)

    assert((min, max) == expected)
  }

  // This test ensures that if we pass an empty list to getMinMaxByYear method,
  // it should throw an IllegalArgumentException
  test("getMinMaxByYear - empty list should throw IllegalArgumentException") {
    assertThrows[IllegalArgumentException] {
      val records = List.empty[CO2Record] // create an empty list of CO2Record objects
      val result = {
        ClimateService.getMinMaxByYear(records, 2021) // call getMinMaxByYear method with empty list and a year
      }
    }
  }

  // This test checks if the filterDecemberData method filters the list correctly
  // and returns only the CO2Record objects for the month of December
  test("filterDecemberData") {
    val record1 = CO2Record(2020, 11, 415.2) // create a CO2Record object for Nov 2020
    val record2 = CO2Record(2021, 12, 417.5) // create a CO2Record object for Dec 2021
    val record3 = CO2Record(2022, 1, 418.9) // create a CO2Record object for Jan 2022
    val record4 = CO2Record(2022, 12, 420.1) // create a CO2Record object for Dec 2022

    val inputList = List(Some(record1), Some(record2), Some(record3), Some(record4)) // create a list of optional CO2Record objects

    val expectedOutput = List(record1, record3) // Expected output after filtering

    val result = ClimateService.filterDecemberData(inputList) // call filterDecemberData method with the list of optional CO2Record objects

    assert(result == expectedOutput) // check if the filtered list of CO2Record objects is equal to the expected output
  }

}
