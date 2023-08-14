
window.onload = function (){
    if(window.location.href.includes("error")){
        document.getElementsByClassName("error_txt")[0].textContent = '아이디 또는 비밀번호를 잘못 입력했습니다.'
        document.getElementsByClassName("error_txt")[0].style.display='block'
    }

    // cookie "ankus_id"가 존재 하는지 여부
    document.querySelector('#check1').checked = document.cookie.includes("ankus_id")

    document.querySelectorAll('.input_box input')[0].addEventListener('keyup',function(e){
        if(e.key === 'Enter') login()
    })
    document.querySelectorAll('.input_box input')[1].addEventListener('keyup',function(e){
        if(e.key === 'Enter') login()
    })
    document.querySelector('.right button').addEventListener('click',function(e){
        login()
    })

    if(document.cookie.includes("ankus_id")) {
        document.querySelector('#check1').checked = true;
    }
    document.body.addEventListener("click",function (e){
        if(e.target.tagName === 'INPUT') {
            if (e.target.parentNode.parentNode.className === "input_box") {
                if(e.target.previousElementSibling.alt === '아이디'){
                    document.querySelectorAll('.input_box img')[0].src = '/common/images/login/click_user.svg'
                    document.querySelectorAll('.input_box img')[1].src = '/common/images/login/pw.svg'
                }else{
                    console.log()
                    document.querySelectorAll('.input_box img')[1].src = '/common/images/login/click_pw.svg'
                    document.querySelectorAll('.input_box img')[0].src = '/common/images/login/user.svg'
                }
            }else{
                document.querySelectorAll('.input_box img')[1].src = '/common/images/login/pw.svg'
                document.querySelectorAll('.input_box img')[0].src = '/common/images/login/user.svg'
            }
        }else{
            document.querySelectorAll('.input_box img')[1].src = '/common/images/login/pw.svg'
            document.querySelectorAll('.input_box img')[0].src = '/common/images/login/user.svg'
        }
    })

}

function login(){
    if(document.querySelectorAll('.input_box input')[0].value !== "" && document.querySelectorAll('.input_box input')[1].value !== ""){
        console.log('login')
        let form = document.createElement('form')
        form.action = '/';
        form.method = 'post';
        form.style.display='none'
        document.body.appendChild(form)

        let id = document.createElement('input')
        id.value = document.querySelectorAll('.input_box input')[0].value
        id.type = 'text'
        id.name = 'username'
        form.appendChild(id)

        let pass = document.createElement('input')
        pass.value = document.querySelectorAll('.input_box input')[1].value
        pass.type = 'password'
        pass.name = 'password'
        form.appendChild(pass)

        let remember_me = document.createElement('input')
        remember_me.checked = document.querySelector('#check1').checked
        remember_me.type = 'checkbox'
        remember_me.name = 'remember-me'
        form.appendChild(remember_me)

        let submit = document.createElement('input')
        submit.type = 'submit'
        form.appendChild(submit)

        submit.click()
    }else{
        if(document.querySelectorAll('.input_box input')[0].value === ""){
            document.getElementsByClassName("error_txt")[0].textContent = '아이디를 입력해주세요'
        }else{
            document.getElementsByClassName("error_txt")[0].textContent = '비밀번호를 입력해주세요'
        }
        document.getElementsByClassName("error_txt")[0].style.display='block'
    }
}
function ajaxCall(){
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let name = $("#username").val();

    let  jsonData= {
        "username" : document.getElementsByName("username")[0].value,
        "password" : document.getElementsByName("password")[0].value,
        "remember-me" : document.getElementsByName("remember-me")[0].value
    }
    console.log(jsonData)

    $.ajax({
        type: 'POST',
        url:'/jupyterlogin',
        data: jsonData, // String -> json 형태로 변환
        beforeSend : function(xhr)
        {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
            xhr.setRequestHeader(header, token);
        },
        success: function(data){
            console.log(data);
        },
        error:function(xhr,status,error){
            console.log('error:'+error);
        }
    });
}
