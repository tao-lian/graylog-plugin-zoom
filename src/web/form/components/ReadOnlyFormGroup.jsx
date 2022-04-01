/*
 * Copyright (C) 2020 Graylog, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Server Side Public License, version 1,
 * as published by MongoDB, Inc.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Server Side Public License for more details.
 *
 * You should have received a copy of the Server Side Public License
 * along with this program. If not, see
 * <http://www.mongodb.com/licensing/server-side-public-license>.
 */
import React from 'react';
import styled, { css } from 'styled-components';

import { Col, Row, HelpBlock } from 'react-bootstrap';
import Icon from './Icon';

/*
// TODO(not important): convert TS Proptypes to JS
type Props = {
  label: React.ReactElement | string,
  value: unknown,
  help?: string,
  className?: string,
};
*/

const ValueCol = styled(Col)`
  padding-top: 7px;
`;

const LabelCol = styled(ValueCol)(({ theme }) => css`
  font-weight: bold;
  @media (min-width: ${theme.breakpoints.min.md}) {
    text-align: right;
  }
`);

const BooleanIcon = styled(Icon)(({ theme, value }) => `
  color: ${value ? theme.colors.variant.success : theme.colors.variant.danger};
`);

const BooleanValue = ({ value }) => (
  <><BooleanIcon name={value ? 'check-circle' : 'times-circle'} value={value} /> {value ? 'yes' : 'no'}</>
);

const readableValue = (value) => {
  if (typeof value === 'boolean') {
    return <BooleanValue value={value} />;
  }

  if (value) {
    return value;
  }

  return '-';
};

/** Displays the provided label and value with the same layout like the FormikFormGroup */
const ReadOnlyFormGroup = ({ label, value, help, className }) => (
  <Row className={className}>
    <LabelCol sm={3}>
      {label}
    </LabelCol>
    <ValueCol sm={9}>
      {readableValue(value)}
      {help && <HelpBlock>{help}</HelpBlock>}
    </ValueCol>
  </Row>
);

ReadOnlyFormGroup.defaultProps = {
  help: undefined,
  className: undefined,
};

export default ReadOnlyFormGroup;