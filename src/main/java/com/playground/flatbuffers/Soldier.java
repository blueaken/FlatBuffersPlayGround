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
public final class Soldier extends Table {
  public static void ValidateVersion() { Constants.FLATBUFFERS_23_5_26(); }
  public static Soldier getRootAsSoldier(ByteBuffer _bb) { return getRootAsSoldier(_bb, new Soldier()); }
  public static Soldier getRootAsSoldier(ByteBuffer _bb, Soldier obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { __reset(_i, _bb); }
  public Soldier __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public String name() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer nameAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public ByteBuffer nameInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 4, 1); }
  public int mana() { int o = __offset(6); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public short hp() { int o = __offset(8); return o != 0 ? bb.getShort(o + bb_pos) : 0; }
  public Weapon weapon() { return weapon(new Weapon()); }
  public Weapon weapon(Weapon obj) { int o = __offset(10); return o != 0 ? obj.__assign(__indirect(o + bb_pos), bb) : null; }

  public static int createSoldier(FlatBufferBuilder builder,
      int nameOffset,
      int mana,
      short hp,
      int weaponOffset) {
    builder.startTable(4);
    Soldier.addWeapon(builder, weaponOffset);
    Soldier.addMana(builder, mana);
    Soldier.addName(builder, nameOffset);
    Soldier.addHp(builder, hp);
    return Soldier.endSoldier(builder);
  }

  public static void startSoldier(FlatBufferBuilder builder) { builder.startTable(4); }
  public static void addName(FlatBufferBuilder builder, int nameOffset) { builder.addOffset(0, nameOffset, 0); }
  public static void addMana(FlatBufferBuilder builder, int mana) { builder.addInt(1, mana, 0); }
  public static void addHp(FlatBufferBuilder builder, short hp) { builder.addShort(2, hp, 0); }
  public static void addWeapon(FlatBufferBuilder builder, int weaponOffset) { builder.addOffset(3, weaponOffset, 0); }
  public static int endSoldier(FlatBufferBuilder builder) {
    int o = builder.endTable();
    return o;
  }
  public static void finishSoldierBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
  public static void finishSizePrefixedSoldierBuffer(FlatBufferBuilder builder, int offset) { builder.finishSizePrefixed(offset); }

  public static final class Vector extends BaseVector {
    public Vector __assign(int _vector, int _element_size, ByteBuffer _bb) { __reset(_vector, _element_size, _bb); return this; }

    public Soldier get(int j) { return get(new Soldier(), j); }
    public Soldier get(Soldier obj, int j) {  return obj.__assign(__indirect(__element(j), bb), bb); }
  }
}

