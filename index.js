/**
 * @flow
 */

import { NativeModules } from 'react-native';

const { HTTPModule: HTTPModuleCore } = NativeModules;

const isURL = (url: String) => url.toLowerCase().contains('http');

class HTTPModule {
  baseURL = '';

  headers = null;

  constructor(baseURL: string, headers: Object = {}) {
    this.baseURL = baseURL;
    this.headers = headers;
  }

  getURL = (url: String) => {
    let URL = url;
    if (!isURL(URL)) {
      URL = this.baseURL + url;
    }
    return URL;
  }

  getHeaders = (headers: Object) => {
    let HEADERS = headers;
    if (!HEADERS) {
      HEADERS = this.headers || {};
    }
    return JSON.stringify(HEADERS);
  }

  getBody = (body: Object) => JSON.stringify(body || {})

  get = (url:String, headers?:Object = {}) => this.request({
    url,
    method: 'get',
    headers,
  })

  post = (url:String, headers?:Object = {},
    body?:Object = {}) => this.request({
    url,
    method: 'post',
    headers,
    body,
  })

  request = ({
    url,
    method,
    headers,
    body,
  }:{
    url:String,
    method: 'get'|'post'|'put'|'delete',
    headers:Object,
    body?:Object,
  }) => HTTPModuleCore.request(this.getURL(url), method, this.getHeaders(headers), this.getBody(body))
}

export default HTTPModule;
