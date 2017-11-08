package com.seuic.android;
import com.seuic.android.MagCardSwipeListener;

interface MagCardReader {
	int startSwipe( MagCardSwipeListener listener, byte key_index, byte track_type, in byte[] track_key, int overtime );
	int stopSwipe();
}
