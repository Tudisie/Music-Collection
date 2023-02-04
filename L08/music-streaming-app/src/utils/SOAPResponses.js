export function getSOAPUsers(jws){
    return '<soap11env:Envelope xmlns:soap11env="http://schemas.xmlsoap.org/soap/envelope/" xmlns:service="services.db-auth.soap">' +
    '<soap11env:Body>' +
        '<service:get_all_users_with_roles>'+
            '<service:jws_obj>'+jws+'</service:jws_obj>'+
        '</service:get_all_users_with_roles>' +
    '</soap11env:Body>' +
    '</soap11env:Envelope>'
}

export function getSOAPUsernameBody(jws){
    return '<soap11env:Envelope xmlns:soap11env="http://schemas.xmlsoap.org/soap/envelope/" xmlns:service="services.db-auth.soap">' +
    '<soap11env:Body>' +
        '<service:get_username_by_jws>'+
            '<service:jws_obj>'+jws+'</service:jws_obj>'+
        '</service:get_username_by_jws>' +
    '</soap11env:Body>' +
    '</soap11env:Envelope>'
}

export function getSOAPUserBody(username, jws){
    return '<soap11env:Envelope xmlns:soap11env="http://schemas.xmlsoap.org/soap/envelope/" xmlns:service="services.db-auth.soap">' +
    '<soap11env:Body>' +
        '<service:get_user_with_roles>'+
            '<service:username>'+username+'</service:username>'+
            '<service:jws_obj>'+jws+'</service:jws_obj>'+
        '</service:get_user_with_roles>' +
    '</soap11env:Body>' +
    '</soap11env:Envelope>'
}

export function getSOAPUserIDBody(jws){
    return '<soap11env:Envelope xmlns:soap11env="http://schemas.xmlsoap.org/soap/envelope/" xmlns:service="services.db-auth.soap">' +
    '<soap11env:Body>' +
        '<service:get_user_id_by_jws>'+
            '<service:jws_obj>'+jws+'</service:jws_obj>'+
        '</service:get_user_id_by_jws>' +
    '</soap11env:Body>' +
    '</soap11env:Envelope>'
}

export function getSOAPLoginBody(username, password){
    return '<soap11env:Envelope xmlns:soap11env="http://schemas.xmlsoap.org/soap/envelope/" xmlns:service="services.db-auth.soap">' +
    '<soap11env:Body>' +
        '<service:login>'+
            '<service:username>'+username+'</service:username>' +
            '<service:password>'+password+'</service:password>' +
        '</service:login>' +
    '</soap11env:Body>' +
    '</soap11env:Envelope>'
}

export function getSOAPRegisterBody(username, password, email, full_name, age){
    return '<soap11env:Envelope xmlns:soap11env="http://schemas.xmlsoap.org/soap/envelope/" xmlns:service="services.db-auth.soap">' +
    '<soap11env:Body>' +
        '<service:create_user>'+
            '<service:username>'+username+'</service:username>' +
            '<service:password>'+password+'</service:password>' +
            '<service:email>'+email+'</service:email>' +
            '<service:full_name>'+full_name+'</service:full_name>' +
            '<service:age>'+age+'</service:age>' +
        '</service:create_user>' +
    '</soap11env:Body>' +
    '</soap11env:Envelope>'
}

export function getSOAPChangePasswordBody(username,password,jws){
    return '<soap11env:Envelope xmlns:soap11env="http://schemas.xmlsoap.org/soap/envelope/" xmlns:service="services.db-auth.soap">' +
    '<soap11env:Body>' +
        '<service:change_password>'+
            '<service:username>'+username+'</service:username>' +
            '<service:new_password>'+password+'</service:new_password>' +
            '<service:jws_obj>'+jws+'</service:jws_obj>' +
        '</service:change_password>' +
    '</soap11env:Body>' +
    '</soap11env:Envelope>'
}

export function getSOAPAddRoleBody(username, roles, jws){
    return '<soap11env:Envelope xmlns:soap11env="http://schemas.xmlsoap.org/soap/envelope/" xmlns:service="services.db-auth.soap">' +
    '<soap11env:Body>' +
        '<service:assign_roles_to_user>'+
            '<service:username>'+username+'</service:username>' +
            '<service:role_list>'+roles+'</service:role_list>' +
            '<service:jws_obj>'+jws+'</service:jws_obj>' +
        '</service:assign_roles_to_user>' +
    '</soap11env:Body>' +
    '</soap11env:Envelope>'
}


export function getSOAPDeleteRoleBody(username, roles, jws){
    return '<soap11env:Envelope xmlns:soap11env="http://schemas.xmlsoap.org/soap/envelope/" xmlns:service="services.db-auth.soap">' +
    '<soap11env:Body>' +
        '<service:remove_roles_from_user>'+
            '<service:username>'+username+'</service:username>' +
            '<service:role_list>'+roles+'</service:role_list>' +
            '<service:jws_obj>'+jws+'</service:jws_obj>' +
        '</service:remove_roles_from_user>' +
    '</soap11env:Body>' +
    '</soap11env:Envelope>'
}

export function getSOAPDeleteUserBody(username, jws){
    return '<soap11env:Envelope xmlns:soap11env="http://schemas.xmlsoap.org/soap/envelope/" xmlns:service="services.db-auth.soap">' +
    '<soap11env:Body>' +
        '<service:delete_user>'+
            '<service:username>'+username+'</service:username>' +
            '<service:jws_obj>'+jws+'</service:jws_obj>' +
        '</service:delete_user>' +
    '</soap11env:Body>' +
    '</soap11env:Envelope>'
}

export function getSOAPUserRolesBody(jws){
    return '<soap11env:Envelope xmlns:soap11env="http://schemas.xmlsoap.org/soap/envelope/" xmlns:service="services.db-auth.soap">' +
    '<soap11env:Body>' +
        '<service:get_roles>'+
            '<service:jws_obj>'+jws+'</service:jws_obj>' +
        '</service:get_roles>' +
    '</soap11env:Body>' +
    '</soap11env:Envelope>'
}

export function SOAPtoJSON(soap){
    const xml = soap[0]["children"];
    var json_obj = {};
    var roles = [];
    for(var x in xml){
        const field  = xml[x].name.replace("s0:","");
        if(field === "roles"){
            roles.push(xml[x].value);
        }
        else{
            json_obj[field] = xml[x].value;
        }
    }

    json_obj["roles"] = roles;
    return JSON.stringify(json_obj);
}

export function SOAPListToJSON(soap){
    const xml = soap["children"];
    var json_obj = {};
    var roles = [];
    for(var x in xml){
        const field  = xml[x].name.replace("s0:","");
        if(field === "roles"){
            roles.push(xml[x].value);
        }
        else{
            json_obj[field] = xml[x].value;
        }
    }

    json_obj["roles"] = roles;
    return JSON.stringify(json_obj);
}