/**
 * @flow
 */

import { NativeModules } from 'react-native';

const { HTTPModule: HTTPModuleCore } = NativeModules;

class HTTPModule {
  get = (url:string, headers?:Object, callback?:Function) => {
    HTTPModuleCore.get(url, JSON.stringify(headers), callback);
  }

  post = (url:string, headers?:Object = {},
    body?:Object = {}, callback?:Function = console.info) => {
    HTTPModuleCore.post(url, JSON.stringify(headers), JSON.stringify(body), callback);
  }
}

export default HTTPModule;
