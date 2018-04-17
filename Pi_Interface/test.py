from Pi_Interface import auth, tokens


test_user = {
    'user': "test_username",
    'pass': "test_password"
}


auth_obj = auth.Authorization()
if auth_obj.signUp(user=test_user):
    print("test successfully completed")
else:
    print("test failed")


status, token = auth_obj.auth(test_user)

if status:
    print("user successfully authenticated and token is %s" % token)
else:
    print("authentication failed")
