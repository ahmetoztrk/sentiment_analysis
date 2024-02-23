import cv2


cap = cv2.VideoCapture(0)

face_cascade = cv2.CascadeClassifier("C:/.../haarcascade_frontalface_default.xml")


while True:
    ret,frame = cap.read()

    frame = cv2.flip(frame,1)

    gray = cv2.cvtColor(frame,cv2.COLOR_BGR2GRAY)

    faces = face_cascade.detectMultiScale(gray,1.4,4)

    for(x,y,w,h) in faces:
        cv2.rectangle(frame,(x,y),(x+w,y+h),(0,255,0),3)
   
    cv2.imshow("Yuz Tespiti",frame)
        
    if cv2.waitKey(1) & 0xFF == ord("q"):
        break

cap.release()
cv2.destroyAllWindows()