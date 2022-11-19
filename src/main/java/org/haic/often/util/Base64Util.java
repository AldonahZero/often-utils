package org.haic.often.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Pattern;

/**
 * Base64编码器,用于Base64的编码和解码
 *
 * @author haicdust
 * @version 1.0
 * @since 2022/2/15 1:05
 */
public class Base64Util {

	/**
	 * 普通字符串转Base64编码的字符串
	 *
	 * @param data 普通字符串
	 * @return base64编码格式的字符串
	 */
	@NotNull
	@Contract(pure = true)
	public static String encode(@NotNull String data) {
		return Base64.getEncoder().encodeToString(data.getBytes());
	}

	/**
	 * Base64编码的字符串转普通字符串
	 *
	 * @param data base64编码格式的字符串
	 * @return 转换后的字符串
	 */
	@NotNull
	@Contract(pure = true)
	public static String decode(@NotNull String data) {
		return decode(data, StandardCharsets.UTF_8);
	}

	/**
	 * Base64编码的字符串转普通字符串
	 *
	 * @param data        base64编码格式的字符串
	 * @param charsetName 需要转换的字符集编码格式
	 * @return 转换后的字符串
	 */
	@NotNull
	@Contract(pure = true)
	public static String decode(@NotNull String data, @NotNull String charsetName) {
		return decode(data, Charset.forName(charsetName));
	}

	/**
	 * Base64编码的字符串转普通字符串
	 *
	 * @param data    base64编码格式的字符串
	 * @param charset 需要转换的字符集编码格式
	 * @return 转换后的字符串
	 */
	@NotNull
	@Contract(pure = true)
	public static String decode(@NotNull String data, Charset charset) {
		return new String(Base64.getDecoder().decode(data), charset);
	}

	/**
	 * 判断字符串是否为Base64编码
	 *
	 * @param str 需要判断的字符串
	 * @return 判断结果
	 */
	@Contract(pure = true)
	public static boolean isBase64(@NotNull String str) {
		return Pattern.matches("^([A-Za-z\\d+/]{4})*([A-Za-z\\d+/]{4}|[A-Za-z\\d+/]{3}=|[A-Za-z\\d+/]{2}==)$", str);
	}

}
