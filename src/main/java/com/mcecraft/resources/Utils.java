package com.mcecraft.resources;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    //https://www.geeksforgeeks.org/sha-1-hash-in-java/
    public static @NotNull String hash(byte[] resourcePack) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            byte[] messageDigest = md.digest(resourcePack);

            BigInteger no = new BigInteger(1, messageDigest);

            String hash = no.toString(16);

            while (hash.length() < 40) {
                hash = "0" + hash;
            }

            return hash;
        }

        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
