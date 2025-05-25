namespace java com.umut.yesevi.thrift


struct AssetThrift {
  1: required double id
  2: required double rationum
  3: required i32 barcode
  4: required i32 itemnum
}


struct AssetList {
  1: list<AssetThrift> assets
}

