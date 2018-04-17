import json
import tokens


'''
USER FORMAT

user = {
	'user':'user-name',
	'pass':'password'
}

'''

class Authorization:

	def __init__():
		print('Entered into authorization')

'''
Return --> bool
True -- if signed up
False -- exception occured or user already exists
'''

	def signUp(self, user):
		user = dict(user)

		if set(['user','pass']).issubset(user.keys()) :
			return False

		fp = open('users.json','r+')
		user_data = json.load(fp).get('users',[])
		users = [i.get('user',None) for i in user_data]

		try:
			if user['user'] in users:
				print('User already exists')
				return False

		except Exception as e:
			print(e)
			print('User format wrong when signing up')
			return False

		user_data.append(user)

		fp.seek(0)
		json.dump({'users':user_data})
		fp.close()
		return True

'''
Return ---> (bool,str)
r[0] -- signed up or not
r[1] -- generated token else None

Check the tokens.py for validating tokens.
'''

	def auth(self, user):
		user = dict(user)
		
		if set(['user','pass']).issubset(user.keys()) :
			return (False,None)

		user_data = json.load(open('users.json')).get('users',[])
		user_index = [i for i in range(len(user_data)) if user_data[i].get('user',None) == user['user']][0]

		if user['pass'] == user_data[user_index]['pass']:
			user_token = tokens.generate_token()
			
			fp = open('user_tokens.json')
			sessions = json.load(fp)
			fp.close()

			sessions.update({user['user'] : user_token})

			return (True,user_token)