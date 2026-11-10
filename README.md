# Lab 09 · Firebase Authentication and Glide

**Môn học** — Mobile Application Programming (71ITSE30603 / 72ITSE30603)  
**Android application ID** — `vn.edu.vlu.lab09`

## 1. Mở project và kết nối Firebase

1. Giải nén ZIP. Trong Android Studio, chọn **File → Open** và mở thư mục `Lab09` có `settings.gradle.kts`. Dùng Gradle JDK 17 hoặc Embedded JDK tương thích, cài **Android SDK Platform 35** và **Build-Tools 34.0.0** trong SDK Manager nếu được yêu cầu.
2. Mở **Terminal** trong Android Studio. Trong PowerShell, chạy `.\gradlew.bat :app:signingReport`. Nếu Java chưa có trong PATH, dùng task **Gradle → app → Tasks → android → signingReport** hoặc đặt `JAVA_HOME` trỏ đến JDK của Android Studio. Sao chép dòng **SHA1** thuộc **Variant: debug**. Chạy trên máy sẽ dùng để build vì mỗi debug keystore có fingerprint riêng.
3. Vào [Firebase Console](https://console.firebase.google.com/), tạo project và thêm ứng dụng **Android**. Nhập package chính xác `vn.edu.vlu.lab09` và SHA-1 ở bước trước. Nếu app đã đăng ký, thêm fingerprint trong **Project settings → Your apps → SHA certificate fingerprints**.
4. Tải `google-services.json` và đặt tại **`Lab09/app/google-services.json`**. Nếu tải nhiều lần, đổi tên file về đúng `google-services.json`.
5. Trong Firebase Console, mở **Build → Authentication → Get started → Sign-in method**. Bật **Email/Password** rồi chọn **Save**.
6. **Sync Project with Gradle Files** rồi chạy trên emulator hoặc máy Android có Internet (API 23 trở lên). Có thể kiểm tra build từ PowerShell bằng `.\gradlew.bat :app:assembleDebug`.

Project giữ Firebase BoM **33.1.2**, `firebase-auth-ktx` và Glide **4.16.0** theo đề. Cấu hình Firebase lấy từ project của bạn. Nếu báo **File google-services.json is missing**, kiểm tra vị trí và tên ở bước 4. Nếu báo **No matching client found for package name**, kiểm tra package đăng ký ở bước 3 và tải lại JSON. Nếu Firebase trả về `ERROR_OPERATION_NOT_ALLOWED`, kiểm tra nhà cung cấp **Email/Password**.

- [Thiết lập Firebase cho Android](https://firebase.google.com/docs/android/setup)
- [Firebase Authentication Email/Password](https://firebase.google.com/docs/auth/android/password-auth)
- [Glide v4 Setup](https://bumptech.github.io/glide/doc/download-setup.html)

## 2. Chạy demo và tự kiểm thử

| Thao tác | Kết quả mong đợi |
| --- | --- |
| Chọn **Register**, nhập email sai, mật khẩu dưới 6 ký tự hoặc xác nhận khác mật khẩu | Hiện lỗi ở ô tương ứng trước khi gọi Firebase. |
| Đăng ký bằng email hợp lệ | Mở gallery, hiện email và avatar. Tài khoản xuất hiện trong **Firebase Authentication → Users**. |
| Cuộn hết danh sách ảnh | Có 10 mục. Glide hiển thị placeholder khi tải và hiệu ứng cross-fade khi ảnh về. |
| Xoay màn hình | Form đăng nhập cuộn được khi mở bàn phím. Gallery đổi số cột theo chiều rộng. |
| Buộc dừng rồi mở lại ứng dụng sau khi đăng nhập | Vào thẳng gallery nhờ `currentUser`. |
| Chọn **Logout** và bấm Back | Không quay lại gallery. |
| Đăng nhập sai mật khẩu hoặc đăng ký lại cùng email | Hiện thông báo lỗi tương ứng. |
| Khi đã đăng xuất, dùng một email nhận thư được và chọn **Forgot password?** | Ứng dụng báo kiểm tra hộp thư. Mở thư Firebase để kiểm tra đường dẫn đặt lại. |
| Tắt mạng rồi thử đăng nhập hoặc mở ảnh chưa có cache | Auth báo kiểm tra kết nối. Ảnh chưa tải được hiện biểu tượng lỗi. Ảnh đã cache có thể vẫn hiển thị. |

Logcat có thể lọc theo package `vn.edu.vlu.lab09` và tag `Lab09Auth` để xem mã lỗi Firebase. Lỗi tải ảnh có thể lọc theo tag `Glide`.

## 3. Chuẩn bị nộp bài

1. Tạo tài khoản riêng cho giảng viên trong **Firebase Authentication → Users → Add user**, hoặc đăng ký trực tiếp từ app rồi đăng xuất và đăng nhập lại để kiểm tra.
2. Dùng **Build → Clean Project**, sau đó xóa `.gradle/`, `build/` và `app/build/` nếu còn. Khi tự nén lại, đặt tên **`Lab09_StudentID_FullName.zip`**, tối đa 25 MB. Nếu giảng viên cần cấu hình để build từ ZIP, chỉ cung cấp cấu hình qua kênh nộp bài riêng.
3. Tạo repo **Public** trên GitHub, để trống lúc khởi tạo. Trong PowerShell mở tại thư mục `Lab09`, chạy các lệnh dưới đây sau khi thay địa chỉ repo. Có thể kiểm tra `git status --short` trước khi commit. File cấu hình Firebase nằm trong `.gitignore`.

   ```powershell
   git init -b main
   git add .
   git status --short
   git commit -m "Complete Lab 09 Firebase Auth and Glide gallery"
   git remote add origin https://github.com/nhan142006/YOUR_REPO.git
   git push -u origin main
   git rev-parse HEAD
   ```

4. Dán thông tin vào ô nộp bài và giữ Firebase project hoạt động đến khi có điểm.

   ```text
   Public GitHub repository: <đường dẫn repo đã push>
   Latest commit hash: <kết quả git rev-parse HEAD>
   Test email: <email tài khoản chấm bài>
   Test password: <mật khẩu tài khoản chấm bài>
   ```

Chỉ điền mật khẩu tài khoản chấm bài trong ô nộp riêng. Ảnh gallery tải từ [Lorem Picsum](https://picsum.photos/). Avatar tải từ [DiceBear](https://www.dicebear.com/integrations/http-api/) bằng seed băm từ email và UID Firebase.
