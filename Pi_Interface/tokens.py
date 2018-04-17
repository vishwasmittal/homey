import json, secrets

def generate_token():
	return secrets.token_hex(16)

'''
Return ---> (bool, str)

r[0] -- validity of token
r[1] -- User detected if r[0] is true else None
'''

def validate_token(token):

	user_tokens = json.load(open('user_tokens.json'))

	for i,k in user_tokens.items():
		if secrets.compare_digest(k,token) :
			return (True, i)

	return (False, None)
