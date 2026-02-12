package com.disorganized.freaks.content.entity;

public interface SturdyFallingBlockEntity {

	default boolean isSturdy() {
		return true;
	}

	default void setSturdy(boolean sturdy) {}

}
