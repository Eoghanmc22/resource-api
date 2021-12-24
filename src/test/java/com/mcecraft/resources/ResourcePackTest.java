package com.mcecraft.resources;

import com.mcecraft.resources.utils.Data;
import com.mcecraft.resources.utils.Loc;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;

class ResourcePackTest {

	@Test
	void test() {
		ResourcePack pack = new ResourcePack();
		pack.include(Loc.any("pack/t.txt"), Data.of("hello"));
		final byte[] bytes = pack.getBytes();
		try (final FileOutputStream fileOutputStream = new FileOutputStream("tests/generatedResourcePackTest.zip")) {
			fileOutputStream.write(bytes);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}