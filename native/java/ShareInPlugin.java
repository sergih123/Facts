package io.github.sergih123.facts;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.webkit.MimeTypeMap;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Receives images and links shared to Facts from other apps ("Share -> Facts").
 * Shared files are copied into the app's cache so the web layer can read them.
 */
@CapacitorPlugin(name = "ShareIn")
public class ShareInPlugin extends Plugin {

    private JSObject pending = null;

    @Override
    public void load() {
        cleanOldFiles();
        JSObject share = readShare(getActivity().getIntent());
        if (share != null) pending = share; // cold start: the page asks with takePending()
    }

    @Override
    protected void handleOnNewIntent(Intent intent) {
        super.handleOnNewIntent(intent);
        JSObject share = readShare(intent);
        if (share != null) notifyListeners("shared", share, true); // app already open
    }

    @PluginMethod
    public void takePending(PluginCall call) {
        JSObject out = new JSObject();
        out.put("has", pending != null);
        if (pending != null) out.put("share", pending);
        pending = null;
        call.resolve(out);
    }

    private JSObject readShare(Intent intent) {
        if (intent == null) return null;
        String action = intent.getAction();
        if (!Intent.ACTION_SEND.equals(action) && !Intent.ACTION_SEND_MULTIPLE.equals(action)) return null;

        ArrayList<Uri> uris = new ArrayList<>();
        if (Intent.ACTION_SEND.equals(action)) {
            Uri u = streamExtra(intent);
            if (u != null) uris.add(u);
        } else {
            ArrayList<Uri> list = streamListExtra(intent);
            if (list != null) uris.addAll(list);
        }

        JSArray files = new JSArray();
        for (Uri u : uris) {
            String mime = mimeOf(u, intent.getType());
            String path = copyToCache(u, mime);
            if (path != null) {
                JSObject f = new JSObject();
                f.put("path", path);
                f.put("mime", mime);
                files.put(f);
            }
        }

        JSObject share = new JSObject();
        share.put("files", files);
        CharSequence text = intent.getCharSequenceExtra(Intent.EXTRA_TEXT);
        if (text != null) share.put("text", text.toString());
        String subject = intent.getStringExtra(Intent.EXTRA_SUBJECT);
        if (subject != null) share.put("subject", subject);

        // Consume the intent so a restart or rotation doesn't add it twice.
        intent.setAction(Intent.ACTION_MAIN);
        intent.removeExtra(Intent.EXTRA_STREAM);
        intent.removeExtra(Intent.EXTRA_TEXT);
        intent.removeExtra(Intent.EXTRA_SUBJECT);
        return share;
    }

    @SuppressWarnings("deprecation")
    private Uri streamExtra(Intent intent) {
        if (Build.VERSION.SDK_INT >= 33) return intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri.class);
        return intent.getParcelableExtra(Intent.EXTRA_STREAM);
    }

    @SuppressWarnings("deprecation")
    private ArrayList<Uri> streamListExtra(Intent intent) {
        if (Build.VERSION.SDK_INT >= 33) return intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM, Uri.class);
        return intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
    }

    private String mimeOf(Uri u, String fallback) {
        String t = null;
        try { t = getContext().getContentResolver().getType(u); } catch (Exception ignored) {}
        if (t == null || t.endsWith("/*")) {
            String ext = MimeTypeMap.getFileExtensionFromUrl(u.toString());
            if (ext != null) t = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext.toLowerCase());
        }
        if (t == null) t = fallback != null ? fallback : "application/octet-stream";
        return t;
    }

    private File sharedDir() {
        File dir = new File(getContext().getCacheDir(), "shared");
        if (!dir.exists()) dir.mkdirs();
        return dir;
    }

    private String copyToCache(Uri u, String mime) {
        String ext = MimeTypeMap.getSingleton().getExtensionFromMimeType(mime);
        File out = new File(sharedDir(), UUID.randomUUID().toString() + "." + (ext != null ? ext : "bin"));
        try (InputStream in = getContext().getContentResolver().openInputStream(u);
             OutputStream os = new FileOutputStream(out)) {
            if (in == null) return null;
            byte[] buf = new byte[64 * 1024];
            int n;
            while ((n = in.read(buf)) > 0) os.write(buf, 0, n);
            return out.getAbsolutePath();
        } catch (Exception e) {
            out.delete();
            return null;
        }
    }

    private void cleanOldFiles() {
        File[] files = sharedDir().listFiles();
        if (files == null) return;
        long cutoff = System.currentTimeMillis() - 24L * 60 * 60 * 1000;
        for (File f : files) if (f.lastModified() < cutoff) f.delete();
    }
}
