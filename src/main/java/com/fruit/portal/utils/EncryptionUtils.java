package com.fruit.portal.utils;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

public final class EncryptionUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptionUtils.class);

    private static final String ENCODING_KEY = "ASCII";

    private static final String ENCODING_TEXT = "UTF-8";

    /**
     * 区别AES算法对空格字符的混淆
     */
    private static final String SPACE_INSTEAD = "~$#!^";

    private static final String SPACE2_INSTEAD = "^#~$!";

    private static final String SPACE2 = String.valueOf((char) 0);

    private static final String SPACELIST = " " + SPACE2;

    /**
     * AES算法使用的KEY在Lion中的配置项名称
     */
    private static final String LION_ENCRYPTION = "encryption.";

    private static final String LION_DPER_KEY = LION_ENCRYPTION + "key_default";

    private static final String LION_DPER_IV = LION_ENCRYPTION + "iv_default";

    private static final String LION_LL_KEY = LION_ENCRYPTION + "key_encrypt";

    private static final String LION_LL_IV = LION_ENCRYPTION + "iv_encrypt";

    public EncryptionUtils()
    {
    }

    public static String encryptWithDotNet(String text)
    {
        if (StringUtils.isBlank(text))
        {
            return "";
        }
        try
        {
            byte[] cipher = encrypt(text.getBytes(ENCODING_TEXT),
                    LION_LL_KEY.getBytes(ENCODING_KEY),
                    LION_LL_IV.getBytes(ENCODING_KEY)
            );
            if (!ArrayUtils.isEmpty(cipher))
            {
                return parseByte2Hex(cipher);
            }
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.error(e.getMessage());
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage());
        }
        return "";
    }

    public static String decryptWithDotNet(String cipher)
    {
        if (StringUtils.isBlank(cipher))
        {
            return "";
        }
        try
        {
            byte[] text = decrypt(parseHex2Byte(cipher),
                    LION_LL_KEY.getBytes(ENCODING_KEY),
                    LION_LL_IV.getBytes(ENCODING_KEY)
            );
            if (!ArrayUtils.isEmpty(text))
            {
                return StringUtils.strip(new String(text, ENCODING_TEXT), SPACELIST);
            }
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.error(e.getMessage());
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage());
        }
        return "";
    }

    /**
     * 使用AES算法,使用指定key和IV对指定字节数组进行加密
     */
    private static byte[] encrypt(byte[] bytes, byte[] key, byte[] iv)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec KeySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, KeySpec, new IvParameterSpec(iv));
            return cipher.doFinal(padWithZeros(bytes));
        }
        catch (Exception e)
        {
            LOGGER.error("e", e);
            return null;
        }
    }

    private static byte[] padWithZeros(byte[] input)
    {
        int rest = input.length % 16;
        if (rest > 0)
        {
            byte[] result = new byte[input.length + (16 - rest)];
            System.arraycopy(input, 0, result, 0, input.length);
            return result;
        }
        return input;
    }

    /**
     * 使用AES算法，使用指定key和IV对指定字节数组进行解密
     */
    private static byte[] decrypt(byte[] bytes, byte[] key, byte[] iv)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec KeySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, KeySpec, new IvParameterSpec(iv));
            return cipher.doFinal(bytes);
        }
        catch (Exception e)
        {
            LOGGER.error("e", e);
            return null;
        }
    }

    private static byte[] parseHex2Byte(String hexText)
    {
        if (StringUtils.isEmpty(hexText))
        {
            return null;
        }
        byte[] result = new byte[hexText.length() / 2];
        for (int i = 0; i < hexText.length() / 2; i++)
        {
            int high = Integer.parseInt(hexText.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexText.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    private static String parseByte2Hex(byte[] bytes)
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++)
        {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1)
            {
                hex = '0' + hex;
            }
            sb.append(hex.toLowerCase());
        }
        return sb.toString();
    }

    /**
     * 解密用户登录信息的cookie[dper]
     *
     * @param cipherText 待解密文本
     * @return
     */
    public static String decrypt4NetAndJava(String cipherText)
    {
        // 空处理
        if (StringUtils.isBlank(cipherText))
        {
            return "";
        }
        try
        {
            byte[] decryptedBytes = decrypt(parseHex2Byte(cipherText),
                    getDperEncryptKey().getBytes(ENCODING_KEY),
                    getDperEncryptIV().getBytes(ENCODING_KEY));
            if (!ArrayUtils.isEmpty(decryptedBytes))
            {
                return StringUtils.strip(new String(decryptedBytes, ENCODING_TEXT), SPACELIST)
                        .replace(SPACE_INSTEAD, " ")
                        .replace(SPACE2_INSTEAD, SPACE2);
            }
        }
        catch (UnsupportedEncodingException e)
        {
            // do nothing
        }
        catch (Exception e)
        {
            LOGGER.error("", e);
        }
        return "";
    }

    /**
     * 获取dper的加密key
     */
    private static String getDperEncryptKey()
    {
        return "aaa";
    }

    /**
     * 获取dper的加密IV
     */
    private static String getDperEncryptIV()
    {
        return "aaa";
    }

}
