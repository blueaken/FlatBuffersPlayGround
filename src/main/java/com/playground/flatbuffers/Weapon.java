// automatically generated by the FlatBuffers compiler, do not modify

package com.playground.flatbuffers;

import com.google.flatbuffers.BaseVector;
import com.google.flatbuffers.BooleanVector;
import com.google.flatbuffers.ByteVector;
import com.google.flatbuffers.Constants;
import com.google.flatbuffers.DoubleVector;
import com.google.flatbuffers.FlatBufferBuilder;
import com.google.flatbuffers.FloatVector;
import com.google.flatbuffers.IntVector;
import com.google.flatbuffers.LongVector;
import com.google.flatbuffers.ShortVector;
import com.google.flatbuffers.StringVector;
import com.google.flatbuffers.Struct;
import com.google.flatbuffers.Table;
import com.google.flatbuffers.UnionVector;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@SuppressWarnings("unused")
public final class Weapon extends Table {
  public static void ValidateVersion() { Constants.FLATBUFFERS_23_5_26(); }
  public static Weapon getRootAsWeapon(ByteBuffer _bb) { return getRootAsWeapon(_bb, new Weapon()); }
  public static Weapon getRootAsWeapon(ByteBuffer _bb, Weapon obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { __reset(_i, _bb); }
  public Weapon __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public String name() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer nameAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public ByteBuffer nameInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 4, 1); }
  public short damage() { int o = __offset(6); return o != 0 ? bb.getShort(o + bb_pos) : 0; }

  public static int createWeapon(FlatBufferBuilder builder,
      int nameOffset,
      short damage) {
    builder.startTable(2);
    Weapon.addName(builder, nameOffset);
    Weapon.addDamage(builder, damage);
    return Weapon.endWeapon(builder);
  }

  public static void startWeapon(FlatBufferBuilder builder) { builder.startTable(2); }
  public static void addName(FlatBufferBuilder builder, int nameOffset) { builder.addOffset(0, nameOffset, 0); }
  public static void addDamage(FlatBufferBuilder builder, short damage) { builder.addShort(1, damage, 0); }
  public static int endWeapon(FlatBufferBuilder builder) {
    int o = builder.endTable();
    return o;
  }

  public static final class Vector extends BaseVector {
    public Vector __assign(int _vector, int _element_size, ByteBuffer _bb) { __reset(_vector, _element_size, _bb); return this; }

    public Weapon get(int j) { return get(new Weapon(), j); }
    public Weapon get(Weapon obj, int j) {  return obj.__assign(__indirect(__element(j), bb), bb); }
  }
}

