$(document).ready(() => {
    setupAjax();

    getUserInfo().then((userInfo) => {
        console.log(userInfo);
    })

    checkToken().then(() => {
        getPage();
    })
});

let getPage = () => {
    $.ajax({
        type: 'GET',
        url: '/api/user',
        success: (response) => {
            console.log('res :: ', response)
        },
        error: (xhr) => {
            if (xhr.status === 401) {
                handleTokenExpiration();
            } else if (xhr.status === 403) {
                window.location.href = '/access-denied';
            } else {
                alert("Unexpected error");
            }
        }
    })
};