import React from 'react';
import PropTypes from 'prop-types';
import lodash from 'lodash';

import Input from './components/Input';
import FormsUtils from './utils/FormUtils';

class ZoomNotificationForm extends React.Component {
  static propTypes = {
    config: PropTypes.object.isRequired,
    validation: PropTypes.object.isRequired,
    onChange: PropTypes.func.isRequired,
  };

  static defaultConfig = {
    webhook: '',
    token: '',
    graylog_url: (document && document.location ? document.location.origin : ''),
    message_template: `\${event.message}: \${if event.timerange_start}
Timerange: \${event.timerange_start} to \${event.timerange_end}\${end}\${if streams}
Streams:\${foreach streams stream} <a href='\${stream.url}'>\${stream.title}</a>\${end}\${end}
\${if backlog}\${foreach backlog message}
\${message.message}
\${end}\${else}No backlog\${end}`,
    proxy_address: '',
    proxy_user: '',
    proxy_password: '',
  };

  propagateChange = (key, value) => {
    const { config, onChange } = this.props;
    const nextConfig = lodash.cloneDeep(config);
    nextConfig[key] = value;
    onChange(nextConfig);
  };

  handleChange = event => {
    const { name } = event.target;
    this.propagateChange(name, FormsUtils.getValueFromInput(event.target));
  };

  handleChatsChange = selectedOptions => {
    this.propagateChange('chats', selectedOptions === '' ? [] : selectedOptions.split(','));
  };

  render() {
    const { config, validation } = this.props;

    return (
      <React.Fragment>
      	<Input id="notification-webhook"
               name="webhook"
               label="Endpoint"
               type="text"
               bsStyle={validation.errors.bot_token ? 'error' : null}
               help={lodash.get(validation, 'errors.webhook[0]', 'Zoom incoming webhook endpoint.')}
               value={config.webhook || ''}
               onChange={this.handleChange}
               required />
               
        <Input id="notification-token"
               name="token"
               label="Token"
               type="text"
               bsStyle={validation.errors.token ? 'error' : null}
               help={lodash.get(validation, 'errors.token[0]', 'Zoom incoming webhook token')}
               value={config.token || ''}
               onChange={this.handleChange}
               required />

        <Input id="notification-graylogURL"
               name="graylog_url"
               label="Graylog URL"
               type="text"
               bsStyle={validation.errors.graylog_url ? 'error' : null}
               help={lodash.get(validation, 'errors.graylog_url[0]', 'URL to your Graylog web interface. Used to build links in alarm notification.')}
               value={config.graylog_url || ''}
               onChange={this.handleChange}
               required />

        
        <Input id="notification-message-template"
               name="message_template"
               label="Message Template"
               type="textarea"
               rows={10}
               bsStyle={validation.errors.message_template ? 'error' : null}
               help={lodash.get(validation, 'errors.message_template[0]', <>See <a href="https://docs.graylog.org/docs/alerts#notifications" target="_blank" rel="noopener">Graylog documentation</a> for more details.</>)}
               value={config.message_template || ''}
               onChange={this.handleChange}
               required />
               
               <Input id="notification-proxy-address"
               name="proxy_address"
               label={<>HTTP Proxy Address <small className="text-muted">(Optional)</small></>}
               type="text"
               bsStyle={validation.errors.proxy_address ? 'error' : null}
               help={lodash.get(validation, 'errors.proxy_address[0]', 'HTTP Proxy Address in the following format: <ProxyAddress>:<Port>')}
               value={config.proxy_address || ''}
               onChange={this.handleChange} />
               
        { config.proxy_address ? <>
        <Input id="notification-proxy-user"
               name="proxy_user"
               label={<>HTTP Proxy User <small className="text-muted">(Optional)</small></>}
               type="text"
               bsStyle={validation.errors.proxy_user ? 'error' : null}
               help={lodash.get(validation, 'errors.proxy_user[0]', '')}
               value={config.proxy_user || ''}
               onChange={this.handleChange} />
        <Input id="notification-proxy-password"
               name="proxy_password"
               label={<>HTTP Proxy Password <small className="text-muted">(Optional)</small></>}
               type="password"
               bsStyle={validation.errors.proxy_password ? 'error' : null}
               help={lodash.get(validation, 'errors.proxy_password[0]', '')}
               value={config.proxy_password || ''}
               onChange={this.handleChange} />
        </> : null }
      </React.Fragment>
    );
  }
}

export default ZoomNotificationForm;