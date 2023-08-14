$(function(){
    document.getElementsByClassName("new_pw")[0].querySelector("input").addEventListener("focusout",function (){
        if(this.value.length < 4){
            document.getElementsByClassName("new_txt")[0].textContent = '4자 이상 입력해주세요.'
            document.getElementsByClassName("new_txt")[0].classList.add("inputColor_n")
            document.getElementsByClassName("new_txt")[0].classList.remove("inputColor_y")
        }else{
            document.getElementsByClassName("new_txt")[0].textContent = '사용할 수 있는 비밀번호입니다.'
            document.getElementsByClassName("new_txt")[0].classList.remove("inputColor_n")
            document.getElementsByClassName("new_txt")[0].classList.add("inputColor_y")
        }
    })

    document.getElementsByClassName("check_pw")[0].querySelector("input").addEventListener("focusout",function (){
        let pw = document.getElementsByClassName("new_pw")[0].querySelector("input").value
        if(this.value.length < 4){
            document.getElementsByClassName("check_txt")[0].textContent = '4자 이상 입력해주세요.'
            document.getElementsByClassName("check_txt")[0].classList.add("inputColor_n")
            document.getElementsByClassName("check_txt")[0].classList.remove("inputColor_y")
        }else if(pw !== this.value){
            document.getElementsByClassName("check_txt")[0].textContent = '비밀번호가 일치하지 않습니다.'
            document.getElementsByClassName("check_txt")[0].classList.add("inputColor_n")
            document.getElementsByClassName("check_txt")[0].classList.remove("inputColor_y")
        }else{
            document.getElementsByClassName("check_txt")[0].textContent = '사용할 수 있는 비밀번호입니다.'
            document.getElementsByClassName("check_txt")[0].classList.remove("inputColor_n")
            document.getElementsByClassName("check_txt")[0].classList.add("inputColor_y")
        }
    })


    // 패스워드 변경
    document.getElementsByClassName("save")[0].addEventListener("click",function(){
        $.ajax({
            type: 'POST',
            url:'passwordChange',
            data: {
                "loginId" : document.getElementsByClassName("id")[0].querySelectorAll("li")[1].textContent,
                "password" : document.getElementsByClassName("pw")[0].querySelector("input").value,
                "newPassword" : document.getElementsByClassName("new_pw")[0].querySelector("input").value,
            },
            success: function(data){
                if(document.getElementsByClassName("new_txt")[0].classList.contains("inputColor_n")){
                    document.getElementsByClassName("new_pw")[0].querySelector("input").focus()
                }else if(document.getElementsByClassName("check_txt")[0].classList.contains("inputColor_n")){
                    document.getElementsByClassName("check_pw")[0].querySelector("input").focus()
                }else{
                    if(!data){
                        document.querySelector(".module_warning_bg .module_warning_txt").textContent = "현재 비밀번호가 일치하지 않습니다."
                        document.querySelector(".module_warning_bg").style.display = 'inline'
                    }else{
                        location.href='/'
                    }
                }

            },
            error:function(error){
                console.log('error:'+error);
            }
        });
    })

    // 취소
    document.getElementsByClassName("cancel")[0].addEventListener("click",function () {
        history.back()
    })

    document.getElementsByClassName("withdraw")[0].addEventListener("click",function(){
        document.getElementsByClassName("delete_modal_bg")[0].style.display = 'inline'
    })
})

// 모달창 관련 이벤트
$(function(){
    // 경고 모달창 버튼 이벤트 처리
    document.getElementsByClassName("module_warning_modal")[0].addEventListener("click",function(e){
        if(e.target.tagName === 'IMG' || e.target.tagName === 'BUTTON'){
            document.getElementsByClassName("module_warning_bg")[0].style.display = 'none'
        }
    })

    // 삭제 모달창 버튼 이벤트 처리
    document.getElementsByClassName("delete_modal")[0].addEventListener("click",function (e){
        if(e.target.tagName === 'IMG' || e.target.textContent === '취소') {
            document.getElementsByClassName("delete_modal_bg")[0].style.display = 'none'
        }else if(e.target.textContent === '삭제'){
            location.href="userDelete?loginId="+document.getElementsByClassName("id")[0].querySelectorAll("li")[1].textContent
        }
    })
})