import auth, tokens

user = {
	'user': 'test1',
	'pass': 'pass2'
}

auth1 = auth.Authorization()
if auth1.signUp(user):
	print('signed up')
else:
	print('not signed up')

s,token = auth1.auth(user)

if s:
	print(token)
else:
	print('Not authed')