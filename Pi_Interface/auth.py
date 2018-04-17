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

  def __init__(self):
    print('Entered into authorization')

  '''
  Return --> bool
  True -- if signed up
  False -- exception occured or user already exists
  '''

  def signUp(self, user):
    user = dict(user)

    if not set(['user','pass']).issubset(user.keys()) :
      return False

    try:
      fp = open('../Users/users.json','r+')
    except:
      fp = open('../Users/users.json','w+')

    try:
      user_data = json.load(fp).get('users',[])
    except:
      user_data = []
    
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
    json.dump({'users':user_data},fp)
    fp.close()
    return True

  '''
  Return ---> (bool,str)
  r[0] -- Logged in or not
  r[1] -- generated token else None

  Check the tokens.py for validating tokens.
  '''

  def auth(self, user):
    print(user)
    user = dict(user)
    
    if not set(['user','pass']).issubset(user.keys()) :
      return (False,None)

    user_data = json.load(open('../Users/users.json')).get('users',[])
    user_index = [i for i in range(len(user_data)) if user_data[i].get('user',None) == user['user']][0]

    if user['pass'] == user_data[user_index]['pass']:
      user_token = tokens.generate_token()
      
      try:
        fp = open('../Users/user_tokens.json','r+')
      except:
        fp = open('../Users/user_tokens.json','w+')
      
      try:
        sessions = json.load(fp)
      except:
        sessions = {}
      sessions.update({user['user'] : user_token})
      
      fp.seek(0)
      json.dump(sessions,fp)
      fp.close()

      return (True,user_token)

    else:
      return (False,None)