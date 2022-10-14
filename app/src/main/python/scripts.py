"""
Created on Sun Feb 21 12:41:18 2021

@author: Tahmid Hasan Fuad (KUET CSE 2K17)
"""
import face_recognition
import numpy as np
import cv2
import base64

def main(data1, data2):
    decoded_data1 = base64.b64decode(data1)
    decoded_data2 = base64.b64decode(data2)

    np_data1 = np.fromstring(decoded_data1, np.uint8)
    np_data2 = np.fromstring(decoded_data2, np.uint8)

    img1 = cv2.imdecode(np_data1, cv2.IMREAD_UNCHANGED)
    img2 = cv2.imdecode(np_data2, cv2.IMREAD_UNCHANGED)

    img_rgb1 = cv2.cvtColor(img1, cv2.COLOR_BGR2RGB)

    img_rgb2 = cv2.cvtColor(img2, cv2.COLOR_BGR2RGB)

    known_image_encoding = face_recognition.face_encodings(img_rgb1)[0]
    unknown_image_encoding = face_recognition.face_encodings(img_rgb2)[0]

    results = face_recognition.compare_faces(
        [known_image_encoding], unknown_image_encoding, tolerance=0.5)

    if results[0]:
        return 1
    else:
        return 0

