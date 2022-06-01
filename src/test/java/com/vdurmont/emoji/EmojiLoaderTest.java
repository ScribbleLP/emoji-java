package com.vdurmont.emoji;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class EmojiLoaderTest {
  @Test
  public void load_empty_database_returns_empty_list() throws IOException {
    // GIVEN
    byte[] bytes = new JSONArray().toString().getBytes("UTF-8");
    InputStream stream = new ByteArrayInputStream(bytes);

    // WHEN
    List<Emoji> emojis = EmojiLoader.loadEmojis(stream);

    // THEN
    assertEquals(0, emojis.size());
  }

  @Test
  public void buildEmojiFromJSON() throws UnsupportedEncodingException {
    // GIVEN
    JSONObject json = new JSONObject("{\r\n"
    		+ "  \"emoji\" : \"😄\",\r\n"
    		+ "  \"description\" : \"grinning face with smiling eyes\",\r\n"
    		+ "  \"category\" : \"Smileys & Emotion\",\r\n"
    		+ "  \"aliases\" : [ \"smile\" ],\r\n"
    		+ "  \"tags\" : [ \"happy\", \"joy\", \"laugh\", \"pleased\" ],\r\n"
    		+ "  \"unicode_version\" : \"6.0\",\r\n"
    		+ "  \"ios_version\" : \"6.0\"\r\n"
    		+ "}");

    // WHEN
    Emoji emoji = EmojiLoader.buildEmojiFromJSON(json);

    // THEN
    assertNotNull(emoji);
    assertEquals("😄", emoji.getUnicode());
    assertEquals(
      "grinning face with smiling eyes",
      emoji.getDescription()
    );
    assertEquals(1, emoji.getAliases().size());
    assertEquals("smile", emoji.getAliases().get(0));
    assertEquals(4, emoji.getTags().size());
    assertEquals("happy", emoji.getTags().get(0));
    assertEquals("joy", emoji.getTags().get(1));
    assertEquals("laugh", emoji.getTags().get(2));
    assertEquals("pleased", emoji.getTags().get(3));
  }

  @Test
  public void buildEmojiFromJSON_without_description_sets_a_null_description()
    throws UnsupportedEncodingException {
    // GIVEN
    JSONObject json = new JSONObject("{\r\n"
    		+ "  \"emoji\" : \"😊\",\r\n"
    		+ "  \"category\" : \"Smileys & Emotion\",\r\n"
    		+ "  \"aliases\" : [ \"blush\" ],\r\n"
    		+ "  \"tags\" : [ \"proud\" ],\r\n"
    		+ "  \"unicode_version\" : \"6.0\",\r\n"
    		+ "  \"ios_version\" : \"6.0\"\r\n"
    		+ "}");

    // WHEN
    Emoji emoji = EmojiLoader.buildEmojiFromJSON(json);

    // THEN
    assertNotNull(emoji);
    assertNull(emoji.getDescription());
  }

  @Test
  public void buildEmojiFromJSON_without_unicode_returns_null()
    throws UnsupportedEncodingException {
    // GIVEN
    JSONObject json = new JSONObject("{\r\n"
    		+ "  \"description\" : \"grinning face\",\r\n"
    		+ "  \"category\" : \"Smileys & Emotion\",\r\n"
    		+ "  \"aliases\" : [ \"grinning\" ],\r\n"
    		+ "  \"tags\" : [ \"smile\", \"happy\" ],\r\n"
    		+ "  \"unicode_version\" : \"6.1\",\r\n"
    		+ "  \"ios_version\" : \"6.0\"\r\n"
    		+ "}");

    // WHEN
    Emoji emoji = EmojiLoader.buildEmojiFromJSON(json);

    // THEN
    assertNull(emoji);
  }

  @Test
  public void buildEmojiFromJSON_computes_the_html_codes()
    throws UnsupportedEncodingException {
    // GIVEN
    JSONObject json = new JSONObject("{\r\n"
    		+ "  \"emoji\" : \"😄\",\r\n"
    		+ "  \"description\" : \"grinning face with smiling eyes\",\r\n"
    		+ "  \"category\" : \"Smileys & Emotion\",\r\n"
    		+ "  \"aliases\" : [ \"smile\" ],\r\n"
    		+ "  \"tags\" : [ \"happy\", \"joy\", \"laugh\", \"pleased\" ],\r\n"
    		+ "  \"unicode_version\" : \"6.0\",\r\n"
    		+ "  \"ios_version\" : \"6.0\"\r\n"
    		+ "}");

    // WHEN
    Emoji emoji = EmojiLoader.buildEmojiFromJSON(json);

    // THEN
    assertNotNull(emoji);
    assertEquals("😄", emoji.getUnicode());
    assertEquals("&#128516;", emoji.getHtmlDecimal());
    assertEquals("&#x1f604;", emoji.getHtmlHexadecimal());
  }

  @Test
  public void buildEmojiFromJSON_with_support_for_fitzpatrick_true()
    throws UnsupportedEncodingException {
    // GIVEN
    JSONObject json = new JSONObject("{\r\n"
    		+ "  \"emoji\" : \"👨\",\r\n"
    		+ "  \"description\" : \"man\",\r\n"
    		+ "  \"category\" : \"People & Body\",\r\n"
    		+ "  \"aliases\" : [ \"man\" ],\r\n"
    		+ "  \"tags\" : [ \"mustache\", \"father\", \"dad\" ],\r\n"
    		+ "  \"unicode_version\" : \"6.0\",\r\n"
    		+ "  \"ios_version\" : \"6.0\",\r\n"
    		+ "  \"skin_tones\" : true\r\n"
    		+ "}");

    // WHEN
    Emoji emoji = EmojiLoader.buildEmojiFromJSON(json);

    // THEN
    assertNotNull(emoji);
    assertTrue(emoji.supportsFitzpatrick());
  }

  @Test
  public void buildEmojiFromJSON_with_support_for_fitzpatrick_false()
    throws UnsupportedEncodingException {
    // GIVEN
    JSONObject json = new JSONObject("{\r\n"
    		+ "  \"emoji\" : \"👨\",\r\n"
    		+ "  \"description\" : \"man\",\r\n"
    		+ "  \"category\" : \"People & Body\",\r\n"
    		+ "  \"aliases\" : [ \"man\" ],\r\n"
    		+ "  \"tags\" : [ \"mustache\", \"father\", \"dad\" ],\r\n"
    		+ "  \"unicode_version\" : \"6.0\",\r\n"
    		+ "  \"ios_version\" : \"6.0\",\r\n"
    		+ "  \"skin_tones\" : false\r\n"
    		+ "}");

    // WHEN
    Emoji emoji = EmojiLoader.buildEmojiFromJSON(json);

    // THEN
    assertNotNull(emoji);
    assertFalse(emoji.supportsFitzpatrick());
  }

  @Test
  public void buildEmojiFromJSON_without_support_for_fitzpatrick()
    throws UnsupportedEncodingException {
    // GIVEN
    JSONObject json = new JSONObject("{\r\n"
    		+ "  \"emoji\" : \"😄\",\r\n"
    		+ "  \"description\" : \"grinning face with smiling eyes\",\r\n"
    		+ "  \"category\" : \"Smileys & Emotion\",\r\n"
    		+ "  \"aliases\" : [ \"smile\" ],\r\n"
    		+ "  \"tags\" : [ \"happy\", \"joy\", \"laugh\", \"pleased\" ],\r\n"
    		+ "  \"unicode_version\" : \"6.0\",\r\n"
    		+ "  \"ios_version\" : \"6.0\"\r\n"
    		+ "}");

    // WHEN
    Emoji emoji = EmojiLoader.buildEmojiFromJSON(json);

    // THEN
    assertNotNull(emoji);
    assertFalse(emoji.supportsFitzpatrick());
  }
}
