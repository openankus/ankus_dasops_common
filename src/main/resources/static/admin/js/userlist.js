

// 모달 팝업
$(function(){
    document.querySelector(".ex_btn img").addEventListener('click',function(){
        document.getElementsByClassName("user_modal_bg")[0].style.display = 'none'
    })
    document.getElementsByClassName("cancel")[0].addEventListener('click',function(){
        document.getElementsByClassName("user_modal_bg")[0].style.display = 'none'
    })
    document.querySelector(".user_modal_bg .save").addEventListener("click",function(){

        let el = document.querySelectorAll(".group p")
        let roleList = []
        for(let i=0;i<el.length;i++){
            if(el[i].querySelector("input").checked) {
                let obj = {}
                obj['name'] = el[i].textContent
                roleList.push(obj)
            }
        }
        $.ajax({
            url: '/userUpdate',
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            data:JSON.stringify({
                loginId: document.querySelectorAll(".user_info .id li")[1].textContent,
                password:document.getElementsByClassName("reset")[0].title === 'true' ,
                enabled : document.getElementsByClassName("activation")[0].querySelectorAll("input")[0].checked,
                roleList:roleList
            }),
            success: function (data) {
                document.getElementsByClassName("user_modal_bg")[0].style.display = 'none'
                table_id.ajax.reload()
            },
            error: function (err) {
                console.log(err)
            }
        });
    })

    // 사용자 삭제
    document.getElementsByClassName("del")[0].addEventListener("click",function(){

        let el = document.getElementsByName("check")
        for(let i=0;i<el.length;i++){
            if(el[i].checked){
                document.getElementsByClassName("delete_modal_bg")[0].style.display = 'inline';
                document.getElementsByClassName("n_delete_modal")[0].style.display = 'none'
                break;
            }
            else if(i === el.length-1){
                document.querySelector(".delete_modal_bg .n_delete_txt").textContent = '삭제할 사용자가 선택되지 않았습니다.'
                document.getElementsByClassName("delete_modal_bg")[0].style.display = 'inline'
                document.getElementsByClassName("n_delete_modal")[0].style.display = 'inline'
            }
        }
    })

    document.getElementsByClassName("delete_modal_bg")[0].addEventListener("click",function(e){
        if(e.target.tagName === 'IMG' || (e.target.tagName === 'BUTTON' && (e.target.textContent === '취소' || e.target.textContent === '확인'))){
            document.getElementsByClassName("delete_modal_bg")[0].style.display = 'none';
        }
        else if(e.target.tagName === 'BUTTON' && e.target.textContent === '삭제'){

            let arr = []
            let adminch = true
            let el = document.getElementsByName("check")
            for(let i=0;i<el.length;i++){
                if(el[i].checked){
                    if(document.getElementsByName("check")[i].parentNode.parentNode.querySelectorAll("td")[1].textContent === 'admin'){
                        adminch = false
                    }
                    arr.push(el[i].value)
                }
            }
            if(adminch){
                $.ajax({
                    url: '/userListDelete',
                    type: "POST",
                    data:{"ids":arr},
                    success: function (data) {
                        table_id.ajax.reload()
                        document.getElementsByClassName("delete_modal_bg")[0].style.display = 'none'
                    },
                    error: function (err) {
                        console.log(err)
                    }
                });
            }else{
                document.querySelector(".delete_modal_bg .n_delete_txt").textContent = '관리자는 삭제할 수 없습니다'
                document.getElementsByClassName("delete_modal_bg")[0].style.display = 'inline'
                document.getElementsByClassName("n_delete_modal")[0].style.display = 'inline'
            }
        }
    })
});
$(function (){
    // 비번 초기화
    document.getElementsByClassName("reset")[0].addEventListener("click",function () {
        this.title = 'true' // 정보수정 후 최종 확인 후 true면 비번 초기화 시킴
        document.querySelector(".module_warning_bg").style.display='block'
    })

    document.querySelector(".module_warning_bg").addEventListener("click",function (e){
        if(e.target.nodeName === 'BUTTON' || e.target.nodeName === 'IMG'){
            document.querySelector(".module_warning_bg").style.display='none'
        }
    })

    // 초기 권한 목록 가져오기
    $.ajax({
        url: '/getNameList',
        type: "POST",
        success: function (data) {
            let t = ''
            for(let i=0;i<data.length;i++){
                t += '<p><input type="checkbox">'+data[i].name+'</p>'
            }
            document.querySelectorAll(".user_modal_bg .group li")[1].innerHTML=t
        },
        error: function (err) {
            console.log(err)
        }
    });
})

// 데이터 테이블
let table_id
$(function () {
    document.querySelector(".search").addEventListener("click", function(e){
        let type = {"아이디": "1", "이름": "2"}
        let numCols = table_id.columns().nodes().length;
        for (let i = 0; i < numCols; i++) {
            if (i != 6) {
                table_id.column(i).search('');
            } else {
                if(document.querySelectorAll("input[name=inp_date]")[0].value > document.querySelectorAll("input[name=inp_date]")[1].value){
                    let d = document.querySelectorAll("input[name=inp_date]")[1].value
                    document.querySelectorAll("input[name=inp_date]")[1].value = document.querySelectorAll("input[name=inp_date]")[0].value
                    document.querySelectorAll("input[name=inp_date]")[0].value = d
                }
                table_id.column(5).search(document.querySelectorAll("input[name=inp_date]")[0].value + " " + document.querySelectorAll("input[name=inp_date]")[1].value);
            }    // 수정일자를 제외한 나머지 값 초기화

        }
        if(document.querySelector(".status_selected-value").textContent==='전체'){
            table_id.column(4).search('*')
        }else if(document.querySelector(".status_selected-value").textContent==='활성화'){
            table_id.column(4).search('true')
        }else{
            table_id.column(4).search('false')
        }


        let searchType = type[document.querySelector(".selected-value").textContent];
        let searchValue = document.querySelector('.keyword input').value;


        table_id.column(searchType).search(searchValue).draw();
    })


    table_id=$('#table_id').DataTable({

        processing: true,
        serverSide: true,
        paging: true,
        searching: true,
        bFilter:false,
        info: false,
        lengthChange: false,
        destroy: true,
        responsive: true,
        order:[1,'asc'],
        orderable: true,
        "ajax": {
            "url": "/user_select_pageable",
            "type": "POST",
            "dataSrc": function (res) {
                document.getElementsByClassName("result")[0].querySelector("span").textContent = res.data.size
                let data = res.data.content;
                for (let i = 0; i < data.length; i++) {
                    data[i]["select"] = data[i].loginId
                    let t = ""
                    for(let r=0;r<data[i].roleList.length;r++){
                        t += data[i].roleList[r]["name"]
                        if(r!==data[i].roleList.length-1){
                            t += ", "
                        }
                    }
                    data[i]["role"] = t
                }
                date_sta_end(res.udate)
                return data;
            }
        },
        columns: [
            {data: 'id'},
            {data: 'loginId'},
            {data: 'name'},
            {data: 'role'},
            {data: 'enabled'},
            {data: 'updateDateTime'},
            {data: 'loginId'},
        ],
        columnDefs: [
            {
                targets: [0, 1, 2, 3, 4, 5, 6],
                className: "dt-center"
            },
            {
                targets: [0],
                width:'5%',
                searchable: false,
                orderable: false,
                render: function (data, type, full, meta) {
                    return '<input  type="checkbox" value="' + data + ' " name="check" >';

                }
            },{
                targets: [1],
                width:'15%',
                searchable: false,
                render: function (data, type, full, meta) {
                    return '<p>'+data+'</p>';
                }
            },
            {
                targets: [2],
                width:'15%',
                searchable: false,
                render: function (data, type, full, meta) {
                    return '<p>'+data+'</p>';
                }
            },
            {
                targets: [3],
                width:'20%',
                searchable: false,
                orderable: false,
                render: function (data, type, full, meta) {
                    return '<p>'+data+'</p>';
                }
            },
            {
                targets: [4],
                width:'10%',
                searchable: false,
                render: function (data, type, full, meta) {
                    if(data){
                        return '<img src="../../common/images/userlist/abled.svg" alt="activate">';
                    }
                    else{
                        return '<img src="../../common/images/userlist/disabled.svg" alt="activate">';
                    }
                }
            },
            {
                targets: [5],
                width:'15%',
                searchable: false,
                render: function (data, type, full, meta) {
                    return '<p>'+data.replace("T"," ").split(".")[0]+'</p>';
                }
            },
            {
                targets: [6],
                width:'15%',
                searchable: false,
                orderable: false,
                className: 'info_btn',
                render: function (data, type, full, meta) {
                    return '<button title='+data+'>정보수정</button>';
                }
            },
        ]
    });
    document.getElementsByClassName("dataTables_filter")[0].style.display='none'
    document.querySelector("#table_id").addEventListener("click",function (e){
        if(e.target.nodeName==='BUTTON' && e.target.innerText==='정보수정') {
            $.ajax({
                url: '/userInfo',
                type: "POST",
                data: {"loginId": e.target.title},
                success: function (data) {
                    document.querySelector(".user_modal_bg").style.display = 'inline'
                    document.querySelectorAll(".user_modal_bg .id li")[1].textContent = data.loginId
                    if(!data.enabled){
                        document.querySelectorAll(".user_modal_bg .activation li")[2].querySelector("input").checked = true
                    }
                    let roleList = data.roleList
                    let el = document.querySelectorAll(".user_modal_bg .group li")[1].querySelectorAll("p")
                    for(let i=0;i<el.length;i++){
                        el[i].querySelector("input").checked = false
                    }
                    for(let r=0;r<roleList.length;r++){
                        for(let i=0;i<el.length;i++){
                            if(roleList[r].name === el[i].textContent){
                                el[i].querySelector("input").checked = true
                            }
                        }
                    }
                },
                error: function (err) {
                    console.log(err)
                }
            });
        }
    })
});

// 전체 체크
$(function(){
    document.querySelector("input[name=checkAll]").addEventListener("change", function (e) {
        e.preventDefault();
        let list = document.querySelectorAll("input[name=check]");
        for (let i = 0; i < list.length; i++) {
            list[i].checked = this.checked;
        }
    });
})

// 검색구분 셀렉트 박스
const selectBoxElements = document.querySelectorAll(".select");

function toggleSelectBox(selectBox) {
    selectBox.classList.toggle("active");
}

function selectOption(optionElement) {
    const selectBox = optionElement.closest(".select");
    const selectedElement = selectBox.querySelector(".selected-value");
    selectedElement.textContent = optionElement.textContent;
    selectedElement.style.color='black'
}

selectBoxElements.forEach(selectBoxElement => {
    selectBoxElement.addEventListener("click", function (e) {
        const targetElement = e.target;
        const isOptionElement = targetElement.classList.contains("option");


        if (isOptionElement) {
            selectOption(targetElement);

        }

        toggleSelectBox(selectBoxElement);
    });
});

document.addEventListener("click", function (e) {
    const targetElement = e.target;
    const isSelect = targetElement.classList.contains("select") || targetElement.closest(".select");

    if (isSelect) {
        return;
    }

    const allSelectBoxElements = document.querySelectorAll(".select");

    allSelectBoxElements.forEach(boxElement => {
        boxElement.classList.remove("active");
    });
});

// 활성화 여부 셀렉트 박스
const selectBoxElements2 = document.querySelectorAll(".status_select");

function toggleSelectBox(selectBox2) {
    selectBox2.classList.toggle("active");
}

function selectOption2(optionElement2) {
    const selectBox2 = optionElement2.closest(".status_select");
    const selectedElement2 = selectBox2.querySelector(".status_selected-value");
    selectedElement2.textContent = optionElement2.textContent;
    selectedElement2.style.color='black'
}

selectBoxElements2.forEach(selectBoxElement2 => {
    selectBoxElement2.addEventListener("click", function (e) {
        const targetElement2 = e.target;
        const isOptionElement2 = targetElement2.classList.contains("option2");


        if (isOptionElement2) {
            selectOption2(targetElement2);

        }

        toggleSelectBox(selectBoxElement2);
    });
});

document.addEventListener("click", function (e) {
    const targetElement2 = e.target;
    const isSelect2 = targetElement2.classList.contains("status_select") || targetElement2.closest(".status_select");

    if (isSelect2) {
        return;
    }

    const allSelectBoxElements2 = document.querySelectorAll(".status_select");

    allSelectBoxElements2.forEach(boxElement2 => {
        boxElement2.classList.remove("active");
    });
});

// 검색 날짜 변경
function date_sta_end(data) {
    document.querySelectorAll("input[name=inp_date]")[0].value=data.split(" ")[0]
    document.querySelectorAll("input[name=inp_date]")[1].value=data.split(" ")[1]
}