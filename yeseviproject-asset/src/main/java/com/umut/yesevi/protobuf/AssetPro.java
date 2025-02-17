// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: asset.proto
// Protobuf Java Version: 4.27.0

package com.umut.yesevi.protobuf;

/**
 * Protobuf type {@code yesevi.AssetPro}
 */
public final class AssetPro extends
    com.google.protobuf.GeneratedMessage implements
    // @@protoc_insertion_point(message_implements:yesevi.AssetPro)
    AssetProOrBuilder {
private static final long serialVersionUID = 0L;
  static {
    com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
      com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
      /* major= */ 4,
      /* minor= */ 27,
      /* patch= */ 0,
      /* suffix= */ "",
      AssetPro.class.getName());
  }
  // Use AssetPro.newBuilder() to construct.
  private AssetPro(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
    super(builder);
  }
  private AssetPro() {
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.umut.yesevi.protobuf.AssetProtos.internal_static_yesevi_AssetPro_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.umut.yesevi.protobuf.AssetProtos.internal_static_yesevi_AssetPro_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.umut.yesevi.protobuf.AssetPro.class, com.umut.yesevi.protobuf.AssetPro.Builder.class);
  }

  private int bitField0_;
  public static final int ID_FIELD_NUMBER = 1;
  private double id_ = 0D;
  /**
   * <code>optional double id = 1;</code>
   * @return Whether the id field is set.
   */
  @java.lang.Override
  public boolean hasId() {
    return ((bitField0_ & 0x00000001) != 0);
  }
  /**
   * <code>optional double id = 1;</code>
   * @return The id.
   */
  @java.lang.Override
  public double getId() {
    return id_;
  }

  public static final int RATIONUM_FIELD_NUMBER = 2;
  private float rationum_ = 0F;
  /**
   * <code>optional float rationum = 2;</code>
   * @return Whether the rationum field is set.
   */
  @java.lang.Override
  public boolean hasRationum() {
    return ((bitField0_ & 0x00000002) != 0);
  }
  /**
   * <code>optional float rationum = 2;</code>
   * @return The rationum.
   */
  @java.lang.Override
  public float getRationum() {
    return rationum_;
  }

  public static final int BARCODE_FIELD_NUMBER = 3;
  private int barcode_ = 0;
  /**
   * <code>optional int32 barcode = 3;</code>
   * @return Whether the barcode field is set.
   */
  @java.lang.Override
  public boolean hasBarcode() {
    return ((bitField0_ & 0x00000004) != 0);
  }
  /**
   * <code>optional int32 barcode = 3;</code>
   * @return The barcode.
   */
  @java.lang.Override
  public int getBarcode() {
    return barcode_;
  }

  public static final int ITEMNUM_FIELD_NUMBER = 4;
  private int itemnum_ = 0;
  /**
   * <code>optional int32 itemnum = 4;</code>
   * @return Whether the itemnum field is set.
   */
  @java.lang.Override
  public boolean hasItemnum() {
    return ((bitField0_ & 0x00000008) != 0);
  }
  /**
   * <code>optional int32 itemnum = 4;</code>
   * @return The itemnum.
   */
  @java.lang.Override
  public int getItemnum() {
    return itemnum_;
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (((bitField0_ & 0x00000001) != 0)) {
      output.writeDouble(1, id_);
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      output.writeFloat(2, rationum_);
    }
    if (((bitField0_ & 0x00000004) != 0)) {
      output.writeInt32(3, barcode_);
    }
    if (((bitField0_ & 0x00000008) != 0)) {
      output.writeInt32(4, itemnum_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (((bitField0_ & 0x00000001) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeDoubleSize(1, id_);
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeFloatSize(2, rationum_);
    }
    if (((bitField0_ & 0x00000004) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(3, barcode_);
    }
    if (((bitField0_ & 0x00000008) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(4, itemnum_);
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.umut.yesevi.protobuf.AssetPro)) {
      return super.equals(obj);
    }
    com.umut.yesevi.protobuf.AssetPro other = (com.umut.yesevi.protobuf.AssetPro) obj;

    if (hasId() != other.hasId()) return false;
    if (hasId()) {
      if (java.lang.Double.doubleToLongBits(getId())
          != java.lang.Double.doubleToLongBits(
              other.getId())) return false;
    }
    if (hasRationum() != other.hasRationum()) return false;
    if (hasRationum()) {
      if (java.lang.Float.floatToIntBits(getRationum())
          != java.lang.Float.floatToIntBits(
              other.getRationum())) return false;
    }
    if (hasBarcode() != other.hasBarcode()) return false;
    if (hasBarcode()) {
      if (getBarcode()
          != other.getBarcode()) return false;
    }
    if (hasItemnum() != other.hasItemnum()) return false;
    if (hasItemnum()) {
      if (getItemnum()
          != other.getItemnum()) return false;
    }
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (hasId()) {
      hash = (37 * hash) + ID_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          java.lang.Double.doubleToLongBits(getId()));
    }
    if (hasRationum()) {
      hash = (37 * hash) + RATIONUM_FIELD_NUMBER;
      hash = (53 * hash) + java.lang.Float.floatToIntBits(
          getRationum());
    }
    if (hasBarcode()) {
      hash = (37 * hash) + BARCODE_FIELD_NUMBER;
      hash = (53 * hash) + getBarcode();
    }
    if (hasItemnum()) {
      hash = (37 * hash) + ITEMNUM_FIELD_NUMBER;
      hash = (53 * hash) + getItemnum();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.umut.yesevi.protobuf.AssetPro parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.umut.yesevi.protobuf.AssetPro parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.umut.yesevi.protobuf.AssetPro parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.umut.yesevi.protobuf.AssetPro parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.umut.yesevi.protobuf.AssetPro parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.umut.yesevi.protobuf.AssetPro parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.umut.yesevi.protobuf.AssetPro parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input);
  }
  public static com.umut.yesevi.protobuf.AssetPro parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static com.umut.yesevi.protobuf.AssetPro parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static com.umut.yesevi.protobuf.AssetPro parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.umut.yesevi.protobuf.AssetPro parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input);
  }
  public static com.umut.yesevi.protobuf.AssetPro parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.umut.yesevi.protobuf.AssetPro prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessage.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code yesevi.AssetPro}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessage.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:yesevi.AssetPro)
      com.umut.yesevi.protobuf.AssetProOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.umut.yesevi.protobuf.AssetProtos.internal_static_yesevi_AssetPro_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.umut.yesevi.protobuf.AssetProtos.internal_static_yesevi_AssetPro_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.umut.yesevi.protobuf.AssetPro.class, com.umut.yesevi.protobuf.AssetPro.Builder.class);
    }

    // Construct using com.umut.yesevi.protobuf.AssetPro.newBuilder()
    private Builder() {

    }

    private Builder(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      super(parent);

    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      id_ = 0D;
      rationum_ = 0F;
      barcode_ = 0;
      itemnum_ = 0;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.umut.yesevi.protobuf.AssetProtos.internal_static_yesevi_AssetPro_descriptor;
    }

    @java.lang.Override
    public com.umut.yesevi.protobuf.AssetPro getDefaultInstanceForType() {
      return com.umut.yesevi.protobuf.AssetPro.getDefaultInstance();
    }

    @java.lang.Override
    public com.umut.yesevi.protobuf.AssetPro build() {
      com.umut.yesevi.protobuf.AssetPro result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.umut.yesevi.protobuf.AssetPro buildPartial() {
      com.umut.yesevi.protobuf.AssetPro result = new com.umut.yesevi.protobuf.AssetPro(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.umut.yesevi.protobuf.AssetPro result) {
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.id_ = id_;
        to_bitField0_ |= 0x00000001;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.rationum_ = rationum_;
        to_bitField0_ |= 0x00000002;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.barcode_ = barcode_;
        to_bitField0_ |= 0x00000004;
      }
      if (((from_bitField0_ & 0x00000008) != 0)) {
        result.itemnum_ = itemnum_;
        to_bitField0_ |= 0x00000008;
      }
      result.bitField0_ |= to_bitField0_;
    }

    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.umut.yesevi.protobuf.AssetPro) {
        return mergeFrom((com.umut.yesevi.protobuf.AssetPro)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.umut.yesevi.protobuf.AssetPro other) {
      if (other == com.umut.yesevi.protobuf.AssetPro.getDefaultInstance()) return this;
      if (other.hasId()) {
        setId(other.getId());
      }
      if (other.hasRationum()) {
        setRationum(other.getRationum());
      }
      if (other.hasBarcode()) {
        setBarcode(other.getBarcode());
      }
      if (other.hasItemnum()) {
        setItemnum(other.getItemnum());
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 9: {
              id_ = input.readDouble();
              bitField0_ |= 0x00000001;
              break;
            } // case 9
            case 21: {
              rationum_ = input.readFloat();
              bitField0_ |= 0x00000002;
              break;
            } // case 21
            case 24: {
              barcode_ = input.readInt32();
              bitField0_ |= 0x00000004;
              break;
            } // case 24
            case 32: {
              itemnum_ = input.readInt32();
              bitField0_ |= 0x00000008;
              break;
            } // case 32
            default: {
              if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                done = true; // was an endgroup tag
              }
              break;
            } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }
    private int bitField0_;

    private double id_ ;
    /**
     * <code>optional double id = 1;</code>
     * @return Whether the id field is set.
     */
    @java.lang.Override
    public boolean hasId() {
      return ((bitField0_ & 0x00000001) != 0);
    }
    /**
     * <code>optional double id = 1;</code>
     * @return The id.
     */
    @java.lang.Override
    public double getId() {
      return id_;
    }
    /**
     * <code>optional double id = 1;</code>
     * @param value The id to set.
     * @return This builder for chaining.
     */
    public Builder setId(double value) {

      id_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>optional double id = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearId() {
      bitField0_ = (bitField0_ & ~0x00000001);
      id_ = 0D;
      onChanged();
      return this;
    }

    private float rationum_ ;
    /**
     * <code>optional float rationum = 2;</code>
     * @return Whether the rationum field is set.
     */
    @java.lang.Override
    public boolean hasRationum() {
      return ((bitField0_ & 0x00000002) != 0);
    }
    /**
     * <code>optional float rationum = 2;</code>
     * @return The rationum.
     */
    @java.lang.Override
    public float getRationum() {
      return rationum_;
    }
    /**
     * <code>optional float rationum = 2;</code>
     * @param value The rationum to set.
     * @return This builder for chaining.
     */
    public Builder setRationum(float value) {

      rationum_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>optional float rationum = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearRationum() {
      bitField0_ = (bitField0_ & ~0x00000002);
      rationum_ = 0F;
      onChanged();
      return this;
    }

    private int barcode_ ;
    /**
     * <code>optional int32 barcode = 3;</code>
     * @return Whether the barcode field is set.
     */
    @java.lang.Override
    public boolean hasBarcode() {
      return ((bitField0_ & 0x00000004) != 0);
    }
    /**
     * <code>optional int32 barcode = 3;</code>
     * @return The barcode.
     */
    @java.lang.Override
    public int getBarcode() {
      return barcode_;
    }
    /**
     * <code>optional int32 barcode = 3;</code>
     * @param value The barcode to set.
     * @return This builder for chaining.
     */
    public Builder setBarcode(int value) {

      barcode_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>optional int32 barcode = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearBarcode() {
      bitField0_ = (bitField0_ & ~0x00000004);
      barcode_ = 0;
      onChanged();
      return this;
    }

    private int itemnum_ ;
    /**
     * <code>optional int32 itemnum = 4;</code>
     * @return Whether the itemnum field is set.
     */
    @java.lang.Override
    public boolean hasItemnum() {
      return ((bitField0_ & 0x00000008) != 0);
    }
    /**
     * <code>optional int32 itemnum = 4;</code>
     * @return The itemnum.
     */
    @java.lang.Override
    public int getItemnum() {
      return itemnum_;
    }
    /**
     * <code>optional int32 itemnum = 4;</code>
     * @param value The itemnum to set.
     * @return This builder for chaining.
     */
    public Builder setItemnum(int value) {

      itemnum_ = value;
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <code>optional int32 itemnum = 4;</code>
     * @return This builder for chaining.
     */
    public Builder clearItemnum() {
      bitField0_ = (bitField0_ & ~0x00000008);
      itemnum_ = 0;
      onChanged();
      return this;
    }

    // @@protoc_insertion_point(builder_scope:yesevi.AssetPro)
  }

  // @@protoc_insertion_point(class_scope:yesevi.AssetPro)
  private static final com.umut.yesevi.protobuf.AssetPro DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.umut.yesevi.protobuf.AssetPro();
  }

  public static com.umut.yesevi.protobuf.AssetPro getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<AssetPro>
      PARSER = new com.google.protobuf.AbstractParser<AssetPro>() {
    @java.lang.Override
    public AssetPro parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      Builder builder = newBuilder();
      try {
        builder.mergeFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(builder.buildPartial());
      } catch (com.google.protobuf.UninitializedMessageException e) {
        throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(e)
            .setUnfinishedMessage(builder.buildPartial());
      }
      return builder.buildPartial();
    }
  };

  public static com.google.protobuf.Parser<AssetPro> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<AssetPro> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.umut.yesevi.protobuf.AssetPro getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

