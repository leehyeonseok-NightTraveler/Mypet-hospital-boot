package com.boot.util;

import java.security.MessageDigest;

/**
 * 이미지 해시 생성을 위한 유틸리티 클래스
 * SHA-256 기반, 앞 16자리 + 하이픈 포맷으로 반환
 */
public class ImageHashUtil {

    /**
     * 파일 바이트 배열로부터 SHA-256 해시 생성 후,
     * 16자리만 사용해 보기 좋게 포맷된 문자열 반환
     * 예: E3B0-C442-98FC-1C14
     */
	public static String getReadableHash(byte[] fileBytes) {
	    try {
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hashBytes = digest.digest(fileBytes);

	        // 해시 HEX 변환
	        StringBuilder sb = new StringBuilder();
	        for (byte b : hashBytes) {
	            sb.append(String.format("%02X", b)); // ← 처음부터 대문자
	        }

	        // 앞 16자리 + 4자리 구분
	        String shortHash = sb.substring(0, 16);
	        return shortHash.replaceAll("(.{4})(?!$)", "$1-");

	    } catch (Exception e) {
	        throw new RuntimeException("이미지 해시 생성 실패", e);
	    }
	}
}
