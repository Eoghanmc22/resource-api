package com.mcecraft.resources;

import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;

class GeneratedResourcePackTest {

	@Test
	void test() {
		GeneratedResourcePack pack = new GeneratedResourcePack();
		pack.include("pack/t.txt", "hello");
		final byte[] bytes = pack.getBytes();
		try (final FileOutputStream fileOutputStream = new FileOutputStream("tests/generatedResourcePackTest.zip");) {
			fileOutputStream.write(bytes);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}