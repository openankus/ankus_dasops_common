function nameCheck() {
    const nameTxt = document.querySelector('.name_txt');
    const inputName = document.querySelector('.input_name').value;
    const checkName = document.getElementById('check_name');
    if (inputName ==='') {
        checkName.innerText = ('필수 입력 정보입니다.');
        nameTxt.classList.add('inputColor_n');
    }
    else {
        checkName.innerText = ('멋진 이름이네요!');
        nameTxt.classList.remove('inputColor_n');
        nameTxt.classList.add('inputColor_y');
    }
}

function idCheck() {
    $.ajax({
        type: 'POST',
        url:'join/id_check',
        data: {"loginId" : document.getElementsByClassName("input_id")[0].value}, // String -> json 형태로 변환
        success: function(data){
            console.log(data);

            const idTxt = document.querySelector('.id_txt');
            const idData = 'admin';
            const inputId = document.querySelector('.input_id').value;
            const checkId = document.getElementById('check_id');

            if (inputId === '') {
                checkId.innerText = ('필수 입력 정보입니다.');
                idTxt.classList.add('inputColor_n');
            }
            else if (data === '') {
                checkId.innerText = ('사용할 수 있는 아이디입니다.');
                idTxt.classList.remove('inputColor_n');
                idTxt.classList.add('inputColor_y');
            }
            else {
                checkId.innerText = ('사용할 수 없는 아이디입니다.');
                idTxt.classList.remove('inputColor_y');
                idTxt.classList.add('inputColor_n');
            }
        },
        error:function(xhr,status,error){
            console.log('error:'+error);
        }
    });

}

function pwCheck() {
    const inputPw = document.querySelector('.input_pw').value;
    const pwTxt = document.querySelector('.pw_txt');
    const count = inputPw.length;
    const checkPw = document.getElementById('check_pw');
    if (count === 0) {
        checkPw.innerText = ('필수 입력 정보입니다.');
        pwTxt.classList.add('inputColor_n');
    }
    else if (count < 4) {
        checkPw.innerText = ('4자 이상 입력해주세요.');
        pwTxt.classList.add('inputColor_n');
    }
    else {
        checkPw.innerText = ('사용할 수 있는 비밀번호입니다.');
        pwTxt.classList.remove('inputColor_n');
        pwTxt.classList.add('inputColor_y');
    }

}

function pwCheck2() {
    const inputSame = document.querySelector('.input_pw').value;
    const inputPw2 = document.querySelector('.input_pw2').value;
    const pwTxt2 = document.querySelector('.pw_txt2');
    const checkPw2 = document.getElementById('check_pw2');

    if (inputPw2.length === 0 || inputSame.length === 0) {
        checkPw2.innerText = ('필수입력 정보입니다.')
        pwTxt2.classList.add('inputColor_n');
    }
    else if(inputPw2.length < 4){
        checkPw2.innerText = ('4자 이상 입력해주세요.')
        pwTxt2.classList.add('inputColor_n');
    }
    else if (inputSame === inputPw2) {
        checkPw2.innerText = ('비밀번호가 일치합니다.');
        pwTxt2.classList.remove('inputColor_n');
        pwTxt2.classList.add('inputColor_y');
    }
    else {
        checkPw2.innerText = ('비밀번호가 일치하지 않습니다.');
        pwTxt2.classList.remove('inputColor_y');
        pwTxt2.classList.add('inputColor_n');
    }
}

const myName = document.querySelector('.input_name');
const id = document.querySelector('.input_id');
const pw = document.querySelector('.input_pw');
const pw2 = document.querySelector('.input_pw2');

myName.addEventListener('focusout',nameCheck);
id.addEventListener('focusout', idCheck);
pw.addEventListener('focusout', pwCheck);
pw2.addEventListener('focusout', pwCheck2);

// 가입완료 팝업
$(function(){
    $('.signup_btn').click(function(){
        let name = document.getElementById("check_name")
        let id = document.getElementById("check_id")
        let pw = document.getElementById("check_pw")
        let pwd = document.getElementById("check_pw2")
        if(!name.classList.contains("inputColor_y") || name.classList.contains("inputColor_n")){
            document.getElementsByClassName("input_name")[0].focus()
        }else if(!id.classList.contains("inputColor_y") || id.classList.contains("inputColor_n")){
            document.getElementsByClassName("input_id")[0].focus()
        }else if(!pw.classList.contains("inputColor_y") || pw.classList.contains("inputColor_n")){
            document.getElementsByClassName("input_pw")[0].focus()
        }else if(!pwd.classList.contains("inputColor_y") || pwd.classList.contains("inputColor_n")){
            document.getElementsByClassName("input_pw2")[0].focus()
        }else{
            $.ajax({
                type: 'POST',
                url:'/join/save',
                data: {
                    "loginId" : document.getElementsByClassName("input_id")[0].value,
                    "name" : document.getElementsByClassName("input_name")[0].value,
                    "password" : document.getElementsByClassName("input_pw")[0].value,
                    "enabled":false,
                    "roleList[0].name":"사용자"
                }, // String -> json 형태로 변환
                success: function(data){
                    if(data==='완료'){
                        $('.complete_modal_bg').stop().fadeIn();
                    }
                },
                error:function(err){
                    console.log('error:'+err);
                }
            });
        }

    });
    $('.complete_modal img').click(function(){
        location.href = "/"
    });
});