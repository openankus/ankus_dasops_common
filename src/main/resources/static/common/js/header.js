// 사용자 메뉴
$(function(){
    document.body.addEventListener("click",function (e) {
        if(e.target.nodeName === 'P' || (e.target.nodeName === 'IMG' && e.target.alt === '사용자이미지')){
            if(e.target.parentNode.className === "lnb_user"){
                if(document.querySelector(".lnb ul").style.display === 'block'){
                    document.querySelector(".lnb ul").style.display = 'none'
                }else{
                    document.querySelector(".lnb ul").style.display = 'block'
                }
            }
        }else if(e.target.className === "lnb_user"){
            if(document.querySelector(".lnb ul").style.display === 'block'){
                document.querySelector(".lnb ul").style.display = 'none'
            }else{
                document.querySelector(".lnb ul").style.display = 'block'
            }
        }else{
            document.querySelector(".lnb ul").style.display = 'none'
        }
    })
    $.ajax({
        type: 'POST',
        url: "/userInfo",
        data: {loginId:document.querySelector(".lnb_user p").textContent},
        success: function (data) {
            document.querySelector(".lnb_user p").name = document.querySelector(".lnb_user p").textContent
            document.querySelector(".lnb_user p").textContent = data["name"]
            document.querySelector(".lnb_user p").style.display='block'
        }, error: function (error) {
            console.log(error)
        }
    });
});