import React, {useState} from 'react';
import {
  Text,
  View,
  Button,
  Picker,
  TextInput,
  StyleSheet,
  Clipboard,
} from 'react-native';
import HTTPModule from 'react-native-http-module-mroads';

const styles = StyleSheet.create({
  container: {flex: 1, padding: 20},
  urlContainer: {
    flexDirection: 'row',
    marginBottom: 20,
    borderColor: 'gray',
    borderWidth: 1,
    height: 40,
  },
  type: {height: 40, width: 100},
  url: {height: 40, width: '72.8%'},
  send: {height: 40, backgroundColor: 'orange'},
  inputText: {
    borderColor: 'gray',
    borderWidth: 1,
    marginBottom: 20,
    textAlignVertical: 'top',
  },
  headers: {height: 100},
  body: {height: 200},
  response: {height: 200},
});

const API = () => {
  const [type, setType] = useState('get');
  const [url, setURL] = useState('');
  const [headers, setHeaders] = useState(null);
  const [body, setBody] = useState(null);
  const [response, setResponse] = useState(null);

  const sendRequest = () => {
    if (!url || !type) {
      return;
    }
    new HTTPModule().request(url, type, headers, body)
      .then(response => setResponse(response))
      .catch(error => setResponse(error.toString()));
  };

  const reset = () => {
    setType('get');
    setURL('');
    setHeaders(null);
    setBody(null);
    setResponse(null);
  };

  return (
    <View style={styles.container}>
      <View style={styles.urlContainer}>
        <Picker
          selectedValue={type}
          style={styles.type}
          onValueChange={itemValue => setType(itemValue)}>
          <Picker.Item label="get" value="get" />
          <Picker.Item label="post" value="post" />
        </Picker>
        <TextInput
          style={styles.url}
          onChangeText={text => setURL(text)}
          value={url}
          placeholder="Enter URL"
        />
        <Button title="SEND" onPress={sendRequest} color="orange" />
      </View>
      <Text>Headers</Text>
      <TextInput
        style={[styles.inputText, styles.headers]}
        onChangeText={text => setHeaders(JSON.parse(text))}
        value={headers}
      />
      <Text>Body</Text>
      <TextInput
        style={[styles.body, styles.inputText]}
        onChangeText={text => setBody(JSON.parse(text))}
        value={body}
      />
      <Text>Response</Text>
      <View>
        <TextInput
          style={[styles.response, styles.inputText]}
          selectable
          editable={false}
          numberOfLines={20}
          onChangeText={text => setResponse(text)}
          value={response}
        />
        <View style={{flexDirection: 'row', justifyContent: 'space-evenly'}}>
          <Button
            title="Copy to clipboard"
            onPress={() => Clipboard.setString(response)}
            color="green"
            disabled={!response}
          />
          <Button title="Make another request" onPress={reset} color="gray" />
        </View>
      </View>
    </View>
  );
};

export default API;
