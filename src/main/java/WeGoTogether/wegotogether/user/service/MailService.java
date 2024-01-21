package WeGoTogether.wegotogether.user.service;

import WeGoTogether.wegotogether.apiPayload.code.status.ErrorStatus;
import WeGoTogether.wegotogether.apiPayload.exception.handler.UserHandler;
import WeGoTogether.wegotogether.user.dto.UserRequestDTO;
import WeGoTogether.wegotogether.user.entity.User;
import WeGoTogether.wegotogether.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;

    @Value("${spring.mail.username}")
    private String senderEmail;
    private static String randNum;

    // 입력된 이메일로 보낼 인증번호 생성
    public SimpleMailMessage CreateMail(String email){

        createNumber();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);   // 수신
        message.setFrom(senderEmail);   // 송신
        message.setSubject("We Go Together 인증 번호");
        message.setText("인증번호는 " + randNum + "입니다.");

        return message;
    }

    // 인증 번호 생성(랜덤 넘버)
    public static void createNumber(){
        randNum = String.format("%06d", new Random().nextInt(1000000));  // 6자리 랜덤 숫자 생성
    }

    public String sendMail(String mail){

        // 입력된 메일이 존재하는 계정이라면
        if (userRepository.findByEmail(mail).isPresent()){

            // SimpleMailMessage는 텍스트 데이터만 전송 가능
            SimpleMailMessage message = CreateMail(mail);

            javaMailSender.send(message);

            // 인증번호 생성 및 필드 업데이트
            User user = userRepository.findByEmail(mail).get();
            updateVerificationFields(user);

            return randNum;
        }
        else{
            throw new UserHandler(ErrorStatus.USER_NOT_FOUND);
        }

    }

    // 인증번호 만료 시간 설정
    private void updateVerificationFields(User user) {
        user.setVerifiedCode(user.getVerifiedCode());
        user.setVerificationCodeGeneratedAt(LocalDateTime.now());
        user.setVerificationCodeExpiration(LocalDateTime.now().plusMinutes(3)); // 3분간 유효
        userRepository.save(user);
    }
}
