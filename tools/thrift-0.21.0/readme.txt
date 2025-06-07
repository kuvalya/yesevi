thrift.exe -r -out [OUTPUT/PATH] --gen java [INPUT/PATH/FILE.thrift]

NOTE1: 
First create [OUTPUT/PATH]

NOTE2:
The -r flag tells Thrift to generate code recursively once it notices includes in a given .thrift file.


ASSET:
thrift-0.21.0.exe -r -out asset --gen java asset.thrift

EMPLOYEE:
thrift-0.21.0.exe -r -out employee --gen java employee.thrift

PERSON:
thrift-0.21.0.exe -r -out person --gen java person.thrift

FILE:
thrift-0.21.0.exe -r -out file --gen java file.thrift