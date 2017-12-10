$.extend({
    getData:function (url,callback) {
        $.ajax({
            url:url,
            type:"get",
            success:function(result) {
                callback(result);
            },
            error:function(e) {
                callback(e);
            }
        });
    },
    postJson:function (url, json, callback) {
        $.ajax({
            url: url,
            data: JSON.stringify(json),
            type: "post",
            contentType: "application/json;charset=utf-8",
            success: function (result) {
                callback(result);
            },
            error:function(e) {
                callback(e);
            }
        });
    },
    postParams: function(url, params, callback) {
        $.ajax({
            url: url,
            data: params,//{a:b}键值对
            type: "post",
            success: function (result) {
                callback(result);
            },
            error:function(e) {
                callback(e);
            }
        });
    }
})