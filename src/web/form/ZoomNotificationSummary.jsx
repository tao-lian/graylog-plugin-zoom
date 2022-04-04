import React from 'react';
import PropTypes from 'prop-types';

import CommonNotificationSummary from './CommonNotificationSummary';

class ZoomNotificationSummary extends React.Component {
  static propTypes = {
    type: PropTypes.string.isRequired,
    notification: PropTypes.object,
    definitionNotification: PropTypes.object.isRequired,
  };

  static defaultProps = {
    notification: {},
  };

  render() {
    const { notification } = this.props;
    return (
      <CommonNotificationSummary {...this.props}>
        <React.Fragment>
          <tr>
            <td>Endpoint</td>
            <td>{notification.config.webhook}</td>
          </tr>
          <tr>
            <td>Token</td>
            <td>{notification.config.token}</td>
          </tr>
          <tr>
            <td>Graylog URL</td>
            <td>{notification.config.graylog_url}</td>
          </tr>
          <tr>
            <td>Message</td>
            <td>{notification.config.message_template}</td>
          </tr>
          { notification.config.proxy_address ? <tr>
            <td>HTTP Proxy Address</td>
            <td>{notification.config.proxy_address}</td>
          </tr> : null}
          { notification.config.proxy_user ? <tr>
            <td>Proxy User</td>
            <td>{notification.config.proxy_user}</td>
          </tr> : null}
          { notification.config.proxy_password ? <tr>
            <td>Proxy Password</td>
            <td><small className="text-muted">(hidden)</small></td>
          </tr> : null}        
        </React.Fragment>
      </CommonNotificationSummary>
    );
  }
}

export default ZoomNotificationSummary;