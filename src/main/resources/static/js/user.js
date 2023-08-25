/**
 * registerUser.html 적용
 */
/* Ajax 이메일 중복 확인 */

document.addEventListener("DOMContentLoaded", function () {
    const inputEmail = document.querySelector('input[name=email]');
    inputEmail.addEventListener('keyup', function () {
        const email = inputEmail.value; //input에 있는 값
        const emailCheck = document.querySelector('.emailCheck'); //결과 문자열이 표현될 영역

        const xhr = new XMLHttpRequest();
        xhr.open('GET', '/kittop/checkEmail?email=' + email); //HTTP 요청 초기화. 통신 방식과 url 설정
        xhr.send(); //url에 요청을 보냄.
        //이벤트 등록. XMLHttpRequest 객체의 readyState 프로퍼티 값이 변할 때마다 자동으로 호출.
        xhr.onreadystatechange = () => {
            //readyState 프로퍼티의 값이 DONE : 요청한 데이터의 처리가 완료되어 응답할 준비가 완료됨.
            if (xhr.readyState !== XMLHttpRequest.DONE) return;

            if (xhr.status === 200) {	//서버(url) 에 문서가 존재함
                const json = JSON.parse(xhr.response);
                if (json.exists) {
                    emailCheck.style.color = 'red';
                    emailCheck.innerHTML = '동일한 아이디가 있습니다.';
                }
                else {
                    emailCheck.style.color = 'green';
                    emailCheck.innerHTML = '동일한 아이디가 없습니다';
                }
            }
            else {	//서버(url)에 문서가 존재하지 않음.
                console.error('Error', xhr.status, xhr.statusText);
            }
        }
    });
});

/* Ajax 닉네임 중복 확인 */

document.addEventListener("DOMContentLoaded", function () {
    const inputNickName = document.querySelector('input[name=nickName]');
    inputNickName.addEventListener('keyup', function () {
        const nickName = inputNickName.value; //input에 있는 값
        const nickNameCheck = document.querySelector('.nickNameCheck'); //결과 문자열이 표현될 영역

        const xhrNick = new XMLHttpRequest();
        xhrNick.open('GET', '/kittop/checkNickName?nickName=' + nickName); //HTTP 요청 초기화. 통신 방식과 url 설정
        xhrNick.send(); //url에 요청을 보냄.
        //이벤트 등록. XMLHttpRequest 객체의 readyState 프로퍼티 값이 변할 때마다 자동으로 호출.
        xhrNick.onreadystatechange = () => {
            //readyState 프로퍼티의 값이 DONE : 요청한 데이터의 처리가 완료되어 응답할 준비가 완료됨.
            if (xhrNick.readyState !== XMLHttpRequest.DONE) return;

            if (xhrNick.status === 200) {	//서버(url) 에 문서가 존재함
                const nickJson = JSON.parse(xhrNick.response);
                if (nickJson.existNicks) {
                    nickNameCheck.style.color = 'red';
                    nickNameCheck.innerHTML = '동일한 닉네임이 있습니다.';
                }
                else {
                    nickNameCheck.style.color = 'green';
                    nickNameCheck.innerHTML = '동일한 닉네임이 없습니다';
                }
            }
            else {	//서버(url)에 문서가 존재하지 않음.
                console.error('Error', xhrNick.status, xhrNick.statusText);
            }
        }
    });
});

/* /Ajax 닉네임 중복 확인 */

/* addr 과 phone 필드에 값 넣기 */

        // 폼 객체 참조
        const form = document.querySelector("form");
        // 폼 제출 이벤트 리스너
        form.addEventListener("submit", function (event) {
            //기본 제출 동작 중지
            event.preventDefault();
            // 각 필드 값 참조
            const email = document.querySelector("input[name='email']").value;
            const password = document.querySelector("input[name='password']").value;
            const nickName = document.querySelector("input[name='nickName']").value;
            const name = document.querySelector("input[name='name']").value;
            const birth = document.querySelector("input[name='birth']").value;
            const gender = document.querySelector("input[name='gender']").value;

            const zipcode = document.querySelector("input[name='zipcode']").value;
            const addr1 = document.querySelector("input[name='addr1']").value;
            const addr2 = document.querySelector("input[name='addr2']").value;
            const phone1 = document.querySelector("input[name='phone1']").value;
            const phone2 = document.querySelector("input[name='phone2']").value;
            const phone3 = document.querySelector("input[name='phone3']").value;

            // addr과 phone 필드에 값을 설정
            document.querySelector("input[name='addr']").value = zipcode + '/' + addr1 + '/' + addr2;
            document.querySelector("input[name='phone']").value = phone1 + '-' + phone2 + '-' + phone3;

            // 폼 제출
            form.submit();
        });

/*/ addr 과 phone 필드에 값 넣기 */

/**
 * registerUser.html 적용  (끝)
 */

/* ********************************************************************************************************************************************************************** */