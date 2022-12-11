package org.haic.often.parser.json;

import org.haic.often.exception.JSONException;
import org.haic.often.util.StringUtil;
import org.haic.often.util.TypeUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author haicdust
 * @version 1.0
 * @since 2022/12/4 23:07
 */
public class JSONObject extends LinkedHashMap<String, Object> {

	public JSONObject() {
		super();
	}

	/**
	 * 这是解析用构建,切勿使用
	 *
	 * @param body    字符串
	 * @param isChild 是否是子类
	 */
	public JSONObject(@NotNull StringBuilder body, boolean isChild) {
		if (body.charAt(0) != '{') {
			throw new JSONException("格式错误: " + body);
		}
		if (body.charAt(1) == '}') { // 分隔符或结束符
			body.delete(0, 2);
			return;
		}
		body.deleteCharAt(0);
		for (int i = 0; i < body.length(); i++) {
			while (Character.isWhitespace(body.charAt(i))) i++; // 跳过空格
			if (body.charAt(i) == ':') throw new JSONException("在下标 " + i + " 处不存在键");
			StringBuilder key = new StringBuilder();
			if (body.charAt(i) == '"') {
				i = StringUtil.interceptString(body, key, i);
			} else {
				while (Character.isLetterOrDigit(body.charAt(i)) || body.charAt(i) == '_') {
					key.append(body.charAt(i++));
				}
			}
			do i++; while (Character.isWhitespace(body.charAt(i))); // 跳过空格

			if (body.charAt(i) != ':') {
				throw new JSONException("解析错误,期望值不为':'");
			}
			do i++; while (Character.isWhitespace(body.charAt(i))); // 跳过空格
			switch (body.charAt(i)) {
				case '"' -> { // 键可能不存在引号
					StringBuilder value = new StringBuilder();
					i = StringUtil.interceptString(body, value, i) + 1;
					this.put(key.toString(), value.toString());
				}
				case 'n' -> {
					if (body.charAt(++i) == 'u' && body.charAt(++i) == 'l' && body.charAt(++i) == 'l') {
						this.put(key.toString(), null);
					} else {
						throw new JSONException("期望值不为'NULL'");
					}
					i++;
				}
				case 't' -> {
					if (body.charAt(++i) == 'r' && body.charAt(++i) == 'u' && body.charAt(++i) == 'e') {
						this.put(key.toString(), true);
					} else {
						throw new JSONException("期望值不为'TRUE'");
					}
					i++;
				}
				case 'f' -> {
					if (body.charAt(++i) == 'a' && body.charAt(++i) == 'l' && body.charAt(++i) == 's' && body.charAt(++i) == 'e') {
						this.put(key.toString(), false);
					} else {
						throw new JSONException("期望值不为'FALSE'");
					}
					i++;
				}
				case '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
					StringBuilder value = new StringBuilder();
					do {
						value.append(body.charAt(i++));
					} while (Character.isDigit(body.charAt(i)) || body.charAt(i) == '.');
					if (body.charAt(i) == 'e' || body.charAt(i) == 'E') { // 自然数
						value.append(body.charAt(i++));
						if (body.charAt(i) == '+') {
							value.append('+');
							i++;
						}
						do {
							value.append(body.charAt(i++));
						} while (Character.isDigit(body.charAt(i)));
					}
					this.put(key.toString(), value);
				}
				case '{' -> {
					body.delete(0, i);
					this.put(key.toString(), new JSONObject(body, true));
					i = 0;
				}
				case '[' -> {
					body.delete(0, i);
					this.put(key.toString(), new JSONArray(body));
					i = 0;
				}
				default -> throw new JSONException("期望值不为'STRING', 'NUMBER', 'NULL', 'TRUE', 'FALSE', '{', '['");
			}
			while (Character.isWhitespace(body.charAt(i))) i++; // 跳过空格
			if (body.charAt(i) == '}') { // 分隔符或结束符
				if (!isChild && i + 1 < body.length()) {
					throw new JSONException("在结束符后仍有字符存在");
				}
				body.delete(0, i + 1);
				return;
			} else if (body.charAt(i) != ',') {
				throw new JSONException("分隔符期待值不为','");
			}
		}
	}

	public static JSONObject parseObject(@NotNull String body) {
		return new JSONObject(new StringBuilder(body.strip()), false);
	}

	public static <K, V> JSONObject parseObject(@NotNull Map<? super K, ? super V> m) {
		JSONObject object = new JSONObject();
		m.forEach((key, value) -> object.put(String.valueOf(key), value));
		return object;
	}

	public <T> T get(@NotNull String key, @NotNull Class<T> itemClass) {
		return TypeUtil.convert(this.get(key), itemClass);
	}

	public Object get(@NotNull String key) {
		return super.get(key);
	}
	
	public Object getOrDefault(@NotNull String key, @NotNull Object value) {
		return super.getOrDefault(key, value);
	}

	public String getString(@NotNull String key) {
		return TypeUtil.convert(this.get(key), String.class);
	}

	public String getStringValue(@NotNull String key, String value) {
		return TypeUtil.convert(super.getOrDefault(key, value), String.class);
	}

	public boolean getBoolean(@NotNull String key) {
		return TypeUtil.convert(this.get(key), Boolean.class);
	}

	public boolean getBooleanValue(@NotNull String key, boolean value) {
		return TypeUtil.convert(super.getOrDefault(key, value), Boolean.class);
	}

	public byte getByte(@NotNull String key) {
		return TypeUtil.convert(this.get(key), Byte.class);
	}

	public byte getByteValue(@NotNull String key, byte value) {
		return TypeUtil.convert(super.getOrDefault(key, value), Byte.class);
	}

	public short getShort(@NotNull String key) {
		return TypeUtil.convert(this.get(key), Short.class);
	}

	public short getShortValue(@NotNull String key, short value) {
		return TypeUtil.convert(super.getOrDefault(key, value), Short.class);
	}

	public int getInteger(@NotNull String key) {
		return TypeUtil.convert(this.get(key), Integer.class);
	}

	public int getIntegerValue(@NotNull String key, int value) {
		return TypeUtil.convert(super.getOrDefault(key, value), Integer.class);
	}

	public long getLong(@NotNull String key) {
		return TypeUtil.convert(this.get(key), Long.class);
	}

	public long getLongValue(@NotNull String key, long value) {
		return TypeUtil.convert(super.getOrDefault(key, value), Long.class);
	}

	public float getFloat(@NotNull String key) {
		return TypeUtil.convert(this.get(key), Float.class);
	}

	public float getFloatValue(@NotNull String key, float value) {
		return TypeUtil.convert(super.getOrDefault(key, value), Float.class);
	}

	public double getDouble(@NotNull String key) {
		return TypeUtil.convert(this.get(key), Double.class);
	}

	public double getDoubleValue(@NotNull String key, double value) {
		return TypeUtil.convert(super.getOrDefault(key, value), Double.class);
	}

	public JSONObject getJSONObject(@NotNull String key) {
		return TypeUtil.convert(this.get(key), JSONObject.class);
	}

	public JSONObject getJSONObjectValue(@NotNull String key, JSONObject value) {
		return TypeUtil.convert(super.getOrDefault(key, value), JSONObject.class);
	}

	public JSONArray getJSONArray(@NotNull String key) {
		return TypeUtil.convert(this.get(key), JSONArray.class);
	}

	public JSONArray getJSONArrayValue(@NotNull String key, JSONArray value) {
		return TypeUtil.convert(super.getOrDefault(key, value), JSONArray.class);
	}

	public <K, V> Map<K, V> toMap(Class<K> keyItemClass, Class<V> valueItemClass) {
		Map<K, V> map = new HashMap<>();
		this.forEach((key, value) -> map.put(TypeUtil.convert(key, keyItemClass), TypeUtil.convert(value, valueItemClass)));
		return map;
	}

	public JSONObject fluentPut(@NotNull String key, Object value) {
		super.put(key, value);
		return this;
	}

	public JSONObject fluentPutAll(@NotNull Map<String, Object> m) {
		super.putAll(m);
		return this;
	}

	@Override
	@SuppressWarnings("DuplicatedCode")
	public String toString() {
		StringBuilder sb = new StringBuilder().append('{');
		for (var token : this.entrySet()) {
			sb.append('"').append(StringUtil.toEscapeString(String.valueOf(token.getKey()))).append("\":");
			Object value = token.getValue();
			if (value instanceof String) {
				sb.append('"').append(StringUtil.toEscapeString((String) value)).append('"');
			} else if (value instanceof JSONArray) {
				sb.append(value);
			} else if (value instanceof List) {
				//noinspection unchecked
				sb.append(JSONArray.parseArray((List<Object>) value));
			} else if (value instanceof JSONObject) {
				sb.append(value);
			} else if (value instanceof Map) {
				//noinspection unchecked
				sb.append(JSONObject.parseObject((Map<String, Object>) value));
			} else {
				sb.append(value);
			}
			sb.append(',');
		}
		if (sb.charAt(sb.length() - 1) == ',') sb.deleteCharAt(sb.length() - 1);
		return sb.append('}').toString();
	}

	/**
	 * 以指定的深度格式化当前标签
	 *
	 * @param depth 深度
	 * @return 格式化的标签
	 */
	@NotNull
	@Contract(pure = true)
	@SuppressWarnings("DuplicatedCode")
	public String toString(int depth) {
		StringBuilder sb = new StringBuilder().append('{');
		for (var token : this.entrySet()) {
			sb.append("\n").append("    ".repeat(depth + 1)).append('"').append(token.getKey()).append("\":");
			Object value = token.getValue();
			if (value instanceof String) {
				StringBuilder valueStr = new StringBuilder();
				char[] chars = ((String) value).toCharArray();
				for (char c : chars) {
					switch (c) {
						case '\\' -> valueStr.append("\\\\");
						case '"' -> valueStr.append("\\\"");
						default -> valueStr.append(c);
					}
				}
				sb.append('"').append(valueStr).append('"');
			} else if (value instanceof JSONArray) {
				sb.append(((JSONArray) value).toString(depth + 1));
			} else if (value instanceof List) {
				//noinspection unchecked
				sb.append(JSONArray.parseArray((List<Object>) value).toString(depth + 1));
			} else if (value instanceof JSONObject) {
				sb.append(((JSONObject) value).toString(depth + 1));
			} else if (value instanceof Map) {
				//noinspection unchecked
				sb.append(JSONObject.parseObject((Map<String, Object>) value).toString(depth + 1));
			} else {
				sb.append(value);
			}
			sb.append(",");
		}
		if (sb.charAt(sb.length() - 1) == ',') sb.deleteCharAt(sb.length() - 1);
		return sb.append("\n").append("    ".repeat(depth)).append('}').toString();
	}

}
