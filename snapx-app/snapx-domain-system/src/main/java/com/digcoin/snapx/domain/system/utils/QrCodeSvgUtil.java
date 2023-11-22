package com.digcoin.snapx.domain.system.utils;

import com.digcoin.snapx.core.common.error.CommonError;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import de.erichseifert.vectorgraphics2d.SVGGraphics2D;
import de.erichseifert.vectorgraphics2d.VectorGraphics2D;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

@Slf4j
public class QrCodeSvgUtil {

    private static final int blockSize = 1;

    private static final int size = 100;

    public static String getQrCodeSvg(String content) {
        try {
            SVGGraphics2D funcOld = new SVGGraphics2D(0, 0, 100 * blockSize, 100 * blockSize);
            QrCodeSvgUtil.fill2VectorLine(funcOld, getBitMatrix(content));
            return funcOld.toString();
        } catch (WriterException e) {
            log.error("QrCodeSvgUtil.getQrCodeSvg error, content:[{}]", content, e);
            throw CommonError.UNEXPECT_ERROR.withDefaults();
        }
    }


    private static void drawLine(VectorGraphics2D funcOld, int x1, int y1, int y2) {
        java.awt.Rectangle s = new java.awt.Rectangle(x1, y1, blockSize, (y2 - y1 + 1) * blockSize);
        funcOld.fill(s);
    }

    private static void fill2VectorLine(VectorGraphics2D funcOld, BitMatrix bitMatrix) {
        if (funcOld == null || bitMatrix == null)
            return;
        double width = bitMatrix.getWidth();
        double height = bitMatrix.getHeight();
        for (int x = 0; x < width; x++) {
            int theX = x * blockSize;
            List<String> tmp = new ArrayList<>();
            int jsq = 0;
            int prev = -1;
            for (int y = 0; y < height; y++) {
                if (bitMatrix.get(x, y)) {
                    if (prev == -1) {
                        jsq++;
                        prev = y;
                        continue;
                    }
                    if (1 == y - prev) {
                        jsq++;
                    } else {
                        tmp.add(String.format("a:%s->%s", (y - jsq), (y)));
                        drawLine(funcOld, theX, (y - jsq), y);
                        jsq = 0;
                    }
                    prev = y;
                } else {
                    if (prev >= 0) {
                        int y1 = (prev - jsq + 1);
                        int y2 = prev;
                        tmp.add(String.format("b:%s->%s", (y1), (y2)));
                        if (y1 == y2) {
                            funcOld.fillRect(theX, y1, blockSize, blockSize);
                        } else {
                            drawLine(funcOld, theX, y1, y2);
                        }
                        jsq = 0;
                        prev = -1;
                    }
                }
            }
            if (jsq > 0) {
                drawLine(funcOld, theX, prev, (prev + jsq - 1));
                tmp.add(String.format("c:%s->%s", (prev), (prev + jsq - 1)));
            }
        }
    }


    private static BitMatrix getBitMatrix(String content) throws WriterException {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1); // 控制码图白边
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M); // 容错率
        return multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, size, size, hints);
    }

}