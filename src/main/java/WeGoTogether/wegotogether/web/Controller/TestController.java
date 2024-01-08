package WeGoTogether.wegotogether.web.Controller;

import WeGoTogether.wegotogether.config.BaseException;
import WeGoTogether.wegotogether.config.BaseResponse;
import WeGoTogether.wegotogether.service.TestService;
import WeGoTogether.wegotogether.web.dto.TestReq;
import WeGoTogether.wegotogether.web.dto.TestRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final TestService testService;


    //test
    @ResponseBody
    @PostMapping("")
    public BaseResponse<TestRes> signUp(@RequestBody TestReq testReq){
        try{
            TestRes testRes = testService.test(testReq);
            return new BaseResponse<>(testRes);
        }catch (BaseException baseException){
            return new BaseResponse<>(baseException.getStatus());
        }
    }
}
