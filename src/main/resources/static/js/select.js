$(document).ready(() => {
    setupAjax();

    getUserInfo().then((userInfo) => {
        console.log(userInfo);
        $('#hiddenUserId').val(userInfo.userId);
        $('#hiddenUserName').val(userInfo.userName);
        $('#hiddenRole').val(userInfo.role);
    })

    checkToken().then(() => {})

    goUserPage();
    goAdminPage();
    goAllPage();

    $('#logout').click((event) => {
        event.preventDefault();

        $.ajax({
            type: 'POST',
            url: '/logout',
            contentType: 'application/json; charset=utf-8',
            success:(response) => {
                alert(response.message);

                localStorage.removeItem('accessToken');

                window.location.href = response.url;

            },
            error: (error) => {
                console.log('logout 오류 발생 :: ', error)
                alert('로그아웃 중 오류가 발생했습니다')
            }
        })
    });
});

let goUserPage = () => {
    $('#user').on('click', (event) => {
        event.preventDefault();
        window.location.href = '/user'
    })
}

let goAdminPage = () => {
    $('#admin').on('click', (event) => {
        event.preventDefault();
        window.location.href = '/admin'
    })
}

let goAllPage = () => {
    $('#all').on('click', (event) => {
        event.preventDefault();
        window.location.href = '/all'
    })
}
