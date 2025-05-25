namespace java com.umut.yesevi.thrift


struct EmployeeThrift {
  1: required double recid
  2: required double share
  3: required i32 jobcode
  4: required string title
}


struct EmployeeList {
  1: list<EmployeeThrift> employees
}

