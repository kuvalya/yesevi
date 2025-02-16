namespace java com.umut.yesevi.thrift


struct PersonThrift {
  1: required string name
  2: required string lastname
  3: required string address
  4: required string city
}


struct PersonList {
  1: list<PersonThrift> persons
}
