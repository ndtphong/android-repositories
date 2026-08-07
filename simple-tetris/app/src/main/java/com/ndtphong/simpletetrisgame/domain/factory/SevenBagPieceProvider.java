package com.ndtphong.simpletetrisgame.domain.factory;

import androidx.annotation.NonNull;

import com.ndtphong.simpletetrisgame.domain.model.TetrominoType;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public final class SevenBagPieceProvider implements PieceProvider {

    private final Queue<TetrominoType> bag = new ArrayDeque<>();

    private final Random random = new Random();

    @NonNull
    @Override
    public TetrominoType next() {
        if (bag.isEmpty()) {
            refill();
        }

        TetrominoType type = bag.poll();

        if (type == null) {
            throw new IllegalStateException("Piece bag is empty");
        }

        return type;
    }

    private void refill() {
        List<TetrominoType> pieces = new ArrayList<>(List.of(TetrominoType.values()));

        Collections.shuffle(pieces, random);
        bag.addAll(pieces);
    }
}
